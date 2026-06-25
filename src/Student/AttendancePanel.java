
package Student;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import Smart_attendance_system.logic.File_Manager;


public class AttendancePanel extends javax.swing.JFrame {
private JTable table;
    private DefaultTableModel model;
    private JLabel overall;
    private String loggedInStudentId;

    public AttendancePanel(String loggedInStudentId) {
        this.loggedInStudentId = loggedInStudentId;

        setTitle("Attendance Portal — " + loggedInStudentId);
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(225, 235, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── HEADER ────────────────────────────────────────────────
        JLabel heading = new JLabel("Attendance Record: " + loggedInStudentId);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(0, 51, 102));
        mainPanel.add(heading, BorderLayout.NORTH);

        // ── TABLE ─────────────────────────────────────────────────
        String[] cols = {"#", "Subject", "Attended", "Leave (Approved)", "Total Classes", "Percentage", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    Object val = getValueAt(row, 6);
                    if (val != null && val.toString().contains("WARNING")) {
                        c.setBackground(new Color(255, 200, 200));
                        c.setForeground(new Color(180, 0, 0));
                    } else {
                        c.setBackground(new Color(220, 255, 220));
                        c.setForeground(new Color(0, 100, 0));
                    }
                }
                return c;
            }
        };
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(0, 51, 102));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Center all columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ── BOTTOM ────────────────────────────────────────────────
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(225, 235, 255));

        overall = new JLabel("Calculating...");
        overall.setFont(new Font("Segoe UI", Font.BOLD, 20));
        overall.setForeground(new Color(0, 51, 102));
        bottom.add(overall, BorderLayout.WEST);

        JLabel note = new JLabel("  * Leave (approved by teacher) counts as Present  ");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(new Color(100, 100, 100));
        bottom.add(note, BorderLayout.EAST);

        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);
        loadAttendanceFromDB(loggedInStudentId);
        setVisible(true);
    }

    private void loadAttendanceFromDB(String studentId) {
        try (Connection con = File_Manager.getConnection()) {
           
            String sql =
                "SELECT subject, " +
                "  SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) AS present_count, " +
                "  SUM(CASE WHEN status='Leave'   THEN 1 ELSE 0 END) AS leave_count, " +
                "  SUM(CASE WHEN status IN ('Present','Absent','Leave') THEN 1 ELSE 0 END) AS total_classes " +
                "FROM attendance " +
                "WHERE student_id = ? " +
                "GROUP BY subject " +
                "ORDER BY subject";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);
            int rowNum       = 1;
            int totalAtt     = 0;
            int totalClasses = 0;
            StringBuilder warnBuilder = new StringBuilder();
            ArrayList<String> warnSubjects = new ArrayList<>();

            while (rs.next()) {
                String subject  = rs.getString("subject");
                int present     = rs.getInt("present_count");
                int leave       = rs.getInt("leave_count");
                int total       = rs.getInt("total_classes");

                // Leave counts as present for percentage
                int effective   = present + leave;
                int pct         = total > 0 ? (effective * 100) / total : 0;

                totalAtt     += effective;
                totalClasses += total;

                String status;
                if (pct >= 75) status = "✓ OK (" + pct + "%)";
                else           status = "⚠ WARNING (" + pct + "%) — Attendance Low!";

                if (pct < 75) {
                    warnSubjects.add(subject);
                    warnBuilder.append("  • ").append(subject)
                               .append(": ").append(pct).append("%\n");
                }

                model.addRow(new Object[]{
                    rowNum++, subject, present, leave, total,
                    pct + "%", status
                });
            }

            // ── Overall % ──────────────────────────────────────────
            if (totalClasses > 0) {
                int overallPct = (totalAtt * 100) / totalClasses;
                overall.setText("Overall Attendance: " + overallPct + "%"
                    + (overallPct < 75 ? "   Below 75%!" : "   Good"));
                overall.setForeground(overallPct < 75
                    ? new Color(180, 0, 0) : new Color(0, 100, 0));
            } else {
                overall.setText("No attendance records found.");
            }

            // ── Email alert if any subject < 75% ───────────────────
            if (!warnSubjects.isEmpty()) {
                String warningText = warnBuilder.toString();
                sendWarningAlert(studentId, warningText);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fetch student email from DB and send warning.
     * If no email column exists, it just logs — no crash.
     */
    private void sendWarningAlert(String studentId, String warningText) {
        try (Connection con = File_Manager.getConnection()) {
            // Try to fetch email — if column doesn't exist, we skip silently
            String sql = "SELECT name FROM users WHERE id = ? AND role = 'Student'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                System.out.println(" LOW ATTENDANCE WARNING for " + name
                    + " (" + studentId + "):\n" + warningText);

                // Show in-app alert
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this,
                        " Low Attendance Warning!\n\n" +
                        "The following subjects are below 75%:\n\n" +
                        warningText +
                        "\nPlease improve your attendance to remain eligible for exams.",
                        "Attendance Warning",
                        JOptionPane.WARNING_MESSAGE));

                // Try to send email (requires jakarta.mail in classpath)
                try {
                    // EmailService.sendCombinedWarning(studentEmail, warningText);
                    // Commented out: add student email column to users table,
                    // then uncomment and replace studentEmail with actual email.
                    System.out.println("Email would be sent to student: " + studentId);
                } catch (Exception emailEx) {
                    System.out.println("Email not sent: " + emailEx.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Could not send warning: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
