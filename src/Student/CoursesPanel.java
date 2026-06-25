
package Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class CoursesPanel extends JFrame {
     public CoursesPanel() {
       
        // FRAME SETTINGS
        setTitle("Courses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // ================= TOP BAR =================
        JPanel topBar = new JPanel();
        topBar.setBounds(0, 0, 900, 70);
        topBar.setBackground(new Color(0, 51, 102));
        topBar.setLayout(null);

        JLabel title = new JLabel(" Enrolled Courses");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(25, 15, 400, 40);

        topBar.add(title);

        // ================= TABLE DATA =================
        String[] cols = {
                "Course",
                "Instructor",
                "Credit Hours",
                "Timing"
        };

        String[][] data = {
                {"Mathematics", "Dr. Ali", "3", "9:00 - 10:30"},
                {"Physics", "Dr. Ahmed", "4", "10:30 - 12:00"},
                {"Computer Science", "Dr. Sara", "3", "12:00 - 1:30"},
                {"English", "Mrs. Khan", "2", "2:00 - 3:00"}
        };

        JTable table = new JTable(data, cols);

        // STYLE TABLE
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0, 51, 102));
        table.getTableHeader().setForeground(Color.WHITE);

        // CENTER ALIGN TEXT
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < cols.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 100, 800, 350);

        // ================= BACKGROUND PANEL =================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(230, 240, 250));
        mainPanel.setBounds(0, 0, 900, 600);

        // CLOSE BUTTON
        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(720, 480, 120, 35);
        closeBtn.setBackground(new Color(0, 51, 102));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);

        closeBtn.addActionListener(e -> dispose());

        // ADD COMPONENTS
        mainPanel.add(topBar);
        mainPanel.add(scroll);
        mainPanel.add(closeBtn);

        add(mainPanel);

        setVisible(true);
    }

       
    

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CoursesPanel.class.getName());


    
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

   
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new CoursesPanel());
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
