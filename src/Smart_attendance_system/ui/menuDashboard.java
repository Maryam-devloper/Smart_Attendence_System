

package Smart_attendance_system.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Smart_attendance_system.logic.File_Manager;

public class menuDashboard extends javax.swing.JPanel {
 private String teacherId;
    private String teacherName;

    // Dynamic labels
    private JLabel lblTotalStudents, lblPendingLeaves,
            lblAllSubmissions, lblTodayAttendance,
            lblAvgPerformance;

    public menuDashboard(String teacherId, String teacherName) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;

        initComponents();

        // Set teacher name
        teachrName.setText(teacherName);

        // Link labels with existing NetBeans labels
        lblTotalStudents = lb_jumlaAnggota1;
        lblPendingLeaves = lb_jumlaAnggota2;
        lblAllSubmissions = lb_jumlaAnggota3;
        lblTodayAttendance = lb_jumlaAnggota4;
        lblAvgPerformance = lb_jumlaAnggota6;

        loadStats();
    }

    // Old constructor support
    public menuDashboard(String teacherName) {
        this.teacherName = teacherName;
        this.teacherId = null;

        initComponents();

        teachrName.setText(teacherName);

        lblTotalStudents = lb_jumlaAnggota1;
        lblPendingLeaves = lb_jumlaAnggota2;
        lblAllSubmissions = lb_jumlaAnggota3;
        lblTodayAttendance = lb_jumlaAnggota4;
        lblAvgPerformance = lb_jumlaAnggota6;

        loadStats();
    }

    public menuDashboard() {
        initComponents();
    }

    private void loadStats() {

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            int totalStudents = 0;
            int pendingLeaves = 0;
            int allSubmissions = 0;

            String todayAtt = "0%";
            String avgPerf = "N/A";

            @Override
            protected Void doInBackground() {

                try (Connection con = File_Manager.getConnection()) {

                    ResultSet rs;

                    // =====================================================
                    // TOTAL STUDENTS FILTERED BY TEACHER DEPARTMENT
                    // =====================================================

                    String deptSql2 =
                            "SELECT department FROM users WHERE id = ?";

                    PreparedStatement dp2 =
                            con.prepareStatement(deptSql2);

                    dp2.setString(1,
                            teacherId != null ? teacherId : "");

                    ResultSet dr2 = dp2.executeQuery();

                    String tDept =
                            dr2.next()
                            ? dr2.getString("department")
                            : null;

                    String countSql =
                            tDept != null
                            ? "SELECT COUNT(*) FROM users WHERE role='Student' AND department=?"
                            : "SELECT COUNT(*) FROM users WHERE role='Student'";

                    PreparedStatement cp =
                            con.prepareStatement(countSql);

                    if (tDept != null) {
                        cp.setString(1, tDept);
                    }

                    ResultSet cr = cp.executeQuery();

                    if (cr.next()) {
                        totalStudents = cr.getInt(1);
                    }

                    // =====================================================
                    // PENDING LEAVES
                    // =====================================================

                    String leaveSql = teacherId != null
                            ? "SELECT COUNT(*) FROM leave_requests WHERE status='Pending' AND teacher_id=?"
                            : "SELECT COUNT(*) FROM leave_requests WHERE status='Pending'";

                    PreparedStatement pst =
                            con.prepareStatement(leaveSql);

                    if (teacherId != null) {
                        pst.setString(1, teacherId);
                    }

                    rs = pst.executeQuery();

                    if (rs.next()) {
                        pendingLeaves = rs.getInt(1);
                    }

                    // =====================================================
                    // ALL SUBMISSIONS
                    // =====================================================

                    String subSql = teacherId != null
                            ? "SELECT COUNT(*) FROM attendance WHERE marked_by=?"
                            : "SELECT COUNT(*) FROM attendance";

                    pst = con.prepareStatement(subSql);

                    if (teacherId != null) {
                        pst.setString(1, teacherName);
                    }

                    rs = pst.executeQuery();

                    if (rs.next()) {
                        allSubmissions = rs.getInt(1);
                    }

                    // =====================================================
                    // TODAY ATTENDANCE %
                    // =====================================================

                    rs = con.createStatement().executeQuery(
                            "SELECT ROUND(" +
                            "SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END)*100.0/COUNT(*),1) " +
                            "FROM attendance WHERE date=CURDATE()"
                    );

                    if (rs.next() && rs.getObject(1) != null) {
                        todayAtt = rs.getDouble(1) + "%";
                    }

                    // =====================================================
                    // AVERAGE PERFORMANCE
                    // =====================================================

                    try {

                        rs = con.createStatement().executeQuery(
                                "SELECT ROUND(AVG(marks),1) FROM results"
                        );

                        if (rs.next() && rs.getObject(1) != null) {
                            avgPerf = rs.getDouble(1) + "%";
                        }

                    } catch (Exception e) {

                        // If results table doesn't exist
                        avgPerf = "N/A";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void done() {

                if (lblTotalStudents != null) {
                    lblTotalStudents.setText(
                            String.valueOf(totalStudents));
                }

                if (lblPendingLeaves != null) {
                    lblPendingLeaves.setText(
                            String.valueOf(pendingLeaves));
                }

                if (lblAllSubmissions != null) {
                    lblAllSubmissions.setText(
                            String.valueOf(allSubmissions));
                }

                if (lblTodayAttendance != null) {
                    lblTodayAttendance.setText(todayAtt);
                }

                if (lblAvgPerformance != null) {
                    lblAvgPerformance.setText(avgPerf);
                }
            }
        };

        worker.execute();
    }

  
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        cardAngotta1 = new javax.swing.JPanel();
        lb_anggota1 = new javax.swing.JLabel();
        lb_jumlaAnggota1 = new javax.swing.JLabel();
        lb_iconAnggota2 = new javax.swing.JLabel();
        lb_iconAngotta3 = new javax.swing.JLabel();
        cardAngotta4 = new javax.swing.JPanel();
        lb_anggota4 = new javax.swing.JLabel();
        lb_jumlaAnggota4 = new javax.swing.JLabel();
        lb_iconAnggota = new javax.swing.JLabel();
        lb_jumlaAnggota5 = new javax.swing.JLabel();
        lb_iconAngotta1 = new javax.swing.JLabel();
        cardAngotta2 = new javax.swing.JPanel();
        lb_anggota2 = new javax.swing.JLabel();
        lb_jumlaAnggota2 = new javax.swing.JLabel();
        lb_iconAnggota1 = new javax.swing.JLabel();
        lb_iconAngotta4 = new javax.swing.JLabel();
        cardAngotta3 = new javax.swing.JPanel();
        lb_anggota3 = new javax.swing.JLabel();
        lb_jumlaAnggota3 = new javax.swing.JLabel();
        lb_iconAnggota3 = new javax.swing.JLabel();
        lb_iconAngotta5 = new javax.swing.JLabel();
        cardAngotta5 = new javax.swing.JPanel();
        lb_anggota5 = new javax.swing.JLabel();
        lb_jumlaAnggota6 = new javax.swing.JLabel();
        lb_iconAnggota4 = new javax.swing.JLabel();
        lb_iconAngotta2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        teachrName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        cardAngotta1.setBackground(new java.awt.Color(0, 0, 153));
        cardAngotta1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        lb_anggota1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_anggota1.setForeground(new java.awt.Color(255, 255, 255));
        lb_anggota1.setText("Total Students");

        lb_jumlaAnggota1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_jumlaAnggota1.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota1.setText("53");

        lb_iconAnggota2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_iconAnggota2.setForeground(new java.awt.Color(255, 255, 255));

        lb_iconAngotta3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/student logo.jpg"))); // NOI18N

        javax.swing.GroupLayout cardAngotta1Layout = new javax.swing.GroupLayout(cardAngotta1);
        cardAngotta1.setLayout(cardAngotta1Layout);
        cardAngotta1Layout.setHorizontalGroup(
            cardAngotta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardAngotta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardAngotta1Layout.createSequentialGroup()
                        .addComponent(lb_anggota1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardAngotta1Layout.createSequentialGroup()
                        .addComponent(lb_iconAnggota2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_iconAngotta3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(lb_jumlaAnggota1)))
                .addContainerGap())
        );
        cardAngotta1Layout.setVerticalGroup(
            cardAngotta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_anggota1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardAngotta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta1Layout.createSequentialGroup()
                        .addComponent(lb_iconAngotta3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(cardAngotta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta1Layout.createSequentialGroup()
                                .addComponent(lb_iconAnggota2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta1Layout.createSequentialGroup()
                                .addComponent(lb_jumlaAnggota1)
                                .addGap(54, 54, 54))))))
        );

        cardAngotta4.setBackground(new java.awt.Color(0, 0, 153));
        cardAngotta4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        lb_anggota4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_anggota4.setForeground(new java.awt.Color(255, 255, 255));
        lb_anggota4.setText("Today's Attendence");

        lb_jumlaAnggota4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_jumlaAnggota4.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota4.setText("89%");

        lb_iconAnggota.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_iconAnggota.setForeground(new java.awt.Color(255, 255, 255));

        lb_jumlaAnggota5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_jumlaAnggota5.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota5.setText("47 student Present");

        lb_iconAngotta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/Atten.jpg"))); // NOI18N

        javax.swing.GroupLayout cardAngotta4Layout = new javax.swing.GroupLayout(cardAngotta4);
        cardAngotta4.setLayout(cardAngotta4Layout);
        cardAngotta4Layout.setHorizontalGroup(
            cardAngotta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardAngotta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta4Layout.createSequentialGroup()
                        .addGap(0, 128, Short.MAX_VALUE)
                        .addComponent(lb_iconAnggota)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(cardAngotta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardAngotta4Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(lb_jumlaAnggota4))
                            .addComponent(lb_jumlaAnggota5)))
                    .addGroup(cardAngotta4Layout.createSequentialGroup()
                        .addGroup(cardAngotta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_anggota4)
                            .addComponent(lb_iconAngotta1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        cardAngotta4Layout.setVerticalGroup(
            cardAngotta4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta4Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(lb_jumlaAnggota4)
                .addGap(18, 18, 18)
                .addComponent(lb_jumlaAnggota5))
            .addGroup(cardAngotta4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_anggota4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_iconAnggota)
                .addGap(8, 8, 8)
                .addComponent(lb_iconAngotta1))
        );

        cardAngotta2.setBackground(new java.awt.Color(0, 0, 153));
        cardAngotta2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 153), 3, true));

        lb_anggota2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_anggota2.setForeground(new java.awt.Color(255, 255, 255));
        lb_anggota2.setText("Pending Leaves");

        lb_jumlaAnggota2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_jumlaAnggota2.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota2.setText("04");

        lb_iconAnggota1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_iconAnggota1.setForeground(new java.awt.Color(255, 255, 255));

        lb_iconAngotta4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/cal.jpg"))); // NOI18N

        javax.swing.GroupLayout cardAngotta2Layout = new javax.swing.GroupLayout(cardAngotta2);
        cardAngotta2.setLayout(cardAngotta2Layout);
        cardAngotta2Layout.setHorizontalGroup(
            cardAngotta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta2Layout.createSequentialGroup()
                .addGroup(cardAngotta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(cardAngotta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_anggota2)
                            .addComponent(lb_iconAnggota1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cardAngotta2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_iconAngotta4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_jumlaAnggota2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardAngotta2Layout.setVerticalGroup(
            cardAngotta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_anggota2)
                .addGroup(cardAngotta2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_iconAngotta4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(cardAngotta2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lb_jumlaAnggota2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(lb_iconAnggota1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardAngotta3.setBackground(new java.awt.Color(0, 0, 153));
        cardAngotta3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 153), 3, true));

        lb_anggota3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_anggota3.setForeground(new java.awt.Color(255, 255, 255));
        lb_anggota3.setText(" All Submissions");

        lb_jumlaAnggota3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_jumlaAnggota3.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota3.setText("38");

        lb_iconAnggota3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_iconAnggota3.setForeground(new java.awt.Color(255, 255, 255));

        lb_iconAngotta5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/cal.jpg"))); // NOI18N

        javax.swing.GroupLayout cardAngotta3Layout = new javax.swing.GroupLayout(cardAngotta3);
        cardAngotta3.setLayout(cardAngotta3Layout);
        cardAngotta3Layout.setHorizontalGroup(
            cardAngotta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(cardAngotta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_iconAngotta5, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addGroup(cardAngotta3Layout.createSequentialGroup()
                        .addGroup(cardAngotta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardAngotta3Layout.createSequentialGroup()
                                .addComponent(lb_anggota3)
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta3Layout.createSequentialGroup()
                                .addComponent(lb_iconAnggota3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(lb_jumlaAnggota3))))
        );
        cardAngotta3Layout.setVerticalGroup(
            cardAngotta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_anggota3)
                .addGroup(cardAngotta3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta3Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(lb_jumlaAnggota3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAngotta3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_iconAngotta5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(lb_iconAnggota3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        cardAngotta5.setBackground(new java.awt.Color(0, 0, 153));
        cardAngotta5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        lb_anggota5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_anggota5.setForeground(new java.awt.Color(255, 255, 255));
        lb_anggota5.setText("Average Exam Performances");

        lb_jumlaAnggota6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_jumlaAnggota6.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlaAnggota6.setText("78%");

        lb_iconAnggota4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lb_iconAnggota4.setForeground(new java.awt.Color(255, 255, 255));

        lb_iconAngotta2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/student logo.jpg"))); // NOI18N

        javax.swing.GroupLayout cardAngotta5Layout = new javax.swing.GroupLayout(cardAngotta5);
        cardAngotta5.setLayout(cardAngotta5Layout);
        cardAngotta5Layout.setHorizontalGroup(
            cardAngotta5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardAngotta5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cardAngotta5Layout.createSequentialGroup()
                        .addComponent(lb_anggota5)
                        .addContainerGap(69, Short.MAX_VALUE))
                    .addGroup(cardAngotta5Layout.createSequentialGroup()
                        .addComponent(lb_iconAnggota4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_iconAngotta2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_jumlaAnggota6)
                        .addGap(23, 23, 23))))
        );
        cardAngotta5Layout.setVerticalGroup(
            cardAngotta5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAngotta5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_anggota5)
                .addGroup(cardAngotta5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardAngotta5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_iconAnggota4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(cardAngotta5Layout.createSequentialGroup()
                        .addGroup(cardAngotta5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardAngotta5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_iconAngotta2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cardAngotta5Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(lb_jumlaAnggota6)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 204));
        jLabel1.setText("Welcome Prof.");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText(" Master Data > Dashboard");

        teachrName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        teachrName.setForeground(new java.awt.Color(51, 0, 204));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Smart_attendance_system/ui/img/dashbord.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(173, 173, 173))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teachrName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addContainerGap(52, Short.MAX_VALUE))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cardAngotta4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardAngotta5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cardAngotta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardAngotta2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardAngotta3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 35, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(teachrName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardAngotta2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardAngotta1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardAngotta3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardAngotta4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardAngotta5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
   
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardAngotta1;
    private javax.swing.JPanel cardAngotta2;
    private javax.swing.JPanel cardAngotta3;
    private javax.swing.JPanel cardAngotta4;
    private javax.swing.JPanel cardAngotta5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lb_anggota1;
    private javax.swing.JLabel lb_anggota2;
    private javax.swing.JLabel lb_anggota3;
    private javax.swing.JLabel lb_anggota4;
    private javax.swing.JLabel lb_anggota5;
    private javax.swing.JLabel lb_iconAnggota;
    private javax.swing.JLabel lb_iconAnggota1;
    private javax.swing.JLabel lb_iconAnggota2;
    private javax.swing.JLabel lb_iconAnggota3;
    private javax.swing.JLabel lb_iconAnggota4;
    private javax.swing.JLabel lb_iconAngotta1;
    private javax.swing.JLabel lb_iconAngotta2;
    private javax.swing.JLabel lb_iconAngotta3;
    private javax.swing.JLabel lb_iconAngotta4;
    private javax.swing.JLabel lb_iconAngotta5;
    private javax.swing.JLabel lb_jumlaAnggota1;
    private javax.swing.JLabel lb_jumlaAnggota2;
    private javax.swing.JLabel lb_jumlaAnggota3;
    private javax.swing.JLabel lb_jumlaAnggota4;
    private javax.swing.JLabel lb_jumlaAnggota5;
    private javax.swing.JLabel lb_jumlaAnggota6;
    private javax.swing.JLabel teachrName;
    // End of variables declaration//GEN-END:variables
}
