
package Student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import Smart_attendance_system.logic.File_Manager;

public class QueryPanel extends JFrame {
 JTabbedPane tabs;
    JPanel listPanel;
    String currentStudentId;
    String currentStudentName;
    HashMap<String, String> teacherNameToIdMap = new HashMap<>();
    JComboBox<String> teacherBox;
    JComboBox<String> subjectBox;

    public QueryPanel(String studentId) {
        this.currentStudentId = studentId;
        fetchStudentName(); // Get student name
        setTitle("Student Query System - " + currentStudentName);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        setupAskTab();
        setupHistoryTab();

        add(tabs);
        loadQueryHistory();
        setVisible(true);
    }
    
    private void fetchStudentName() {
        try (Connection con = File_Manager.getConnection()) {
            String sql = "SELECT name FROM users WHERE id = ? AND role = 'Student'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentStudentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                currentStudentName = rs.getString("name");
            } else {
                currentStudentName = currentStudentId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentStudentName = currentStudentId;
        }
    }

    private void setupAskTab() {
        JPanel askPanel = new JPanel(null);
        askPanel.setBackground(new Color(245, 245, 250));

        JLabel heading = new JLabel("Ask a Question to Your Teacher");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setBounds(40, 20, 400, 40);
        heading.setForeground(new Color(0, 51, 102));

        JLabel lblT = new JLabel("Select Teacher:");
        lblT.setBounds(40, 80, 120, 30);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        teacherBox = new JComboBox<>();
        teacherBox.setBounds(180, 80, 250, 30);
        teacherBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblS = new JLabel("Subject:");
        lblS.setBounds(40, 130, 120, 30);
        lblS.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subjectBox = new JComboBox<>();
        subjectBox.setBounds(180, 130, 250, 30);
        subjectBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblQ = new JLabel("Your Question:");
        lblQ.setBounds(40, 180, 120, 30);
        lblQ.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JTextArea queryArea = new JTextArea();
        queryArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        queryArea.setLineWrap(true);
        queryArea.setWrapStyleWord(true);
        queryArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scroll = new JScrollPane(queryArea);
        scroll.setBounds(40, 210, 600, 150);

        JButton btnSend = new JButton("Send Query");
        btnSend.setBounds(40, 380, 180, 40);
        btnSend.setBackground(new Color(0, 102, 204));
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSend.setFocusPainted(false);

        // Load teachers from database
        loadTeachersFromDB();

        // Update subject box when a teacher is selected
        teacherBox.addActionListener(e -> updateSubjectsForSelectedTeacher());

        btnSend.addActionListener(e -> {
            String teacherName = (String) teacherBox.getSelectedItem();
            String tId = teacherNameToIdMap.get(teacherName);
            String subject = (String) subjectBox.getSelectedItem();
            String question = queryArea.getText().trim();
            
            if (teacherName == null || tId == null) {
                JOptionPane.showMessageDialog(this, "Please select a teacher!");
                return;
            }
            
            if (subject == null || subject.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a subject!");
                return;
            }
            
            if (question.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your question!");
                return;
            }

            try (Connection con = File_Manager.getConnection()) {
                String sql = "INSERT INTO student_queries (student_id, student_name, teacher_id, teacher_name, subject, question, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, currentStudentId);
                pst.setString(2, currentStudentName);
                pst.setString(3, tId);
                pst.setString(4, teacherName);
                pst.setString(5, subject);
                pst.setString(6, question);
                
                int rowsInserted = pst.executeUpdate();
                
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Query sent to " + teacherName + " successfully!");
                    queryArea.setText("");
                    loadQueryHistory(); // Refresh the history tab
                    tabs.setSelectedIndex(1); // Switch to history tab
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send query. Please try again.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        askPanel.add(heading);
        askPanel.add(lblT);
        askPanel.add(teacherBox);
        askPanel.add(lblS);
        askPanel.add(subjectBox);
        askPanel.add(lblQ);
        askPanel.add(scroll);
        askPanel.add(btnSend);
        
        tabs.addTab("New Query", askPanel);
    }

    private void loadTeachersFromDB() {
       try (Connection con = File_Manager.getConnection()) {
        // Get student's department
        String studentDept = null;
        String deptSql = "SELECT department FROM users WHERE id = ? AND role = 'Student'";
        PreparedStatement dp = con.prepareStatement(deptSql);
        dp.setString(1, currentStudentId);
        ResultSet dr = dp.executeQuery();
        if (dr.next()) studentDept = dr.getString("department");

        // Only load teachers from same department
        String sql = studentDept != null
            ? "SELECT id, name FROM users WHERE role = 'Teacher' AND department = ?"
            : "SELECT id, name FROM users WHERE role = 'Teacher'";

        PreparedStatement pst = con.prepareStatement(sql);
        if (studentDept != null) pst.setString(1, studentDept);
        ResultSet rs = pst.executeQuery();

        teacherBox.removeAllItems();
        teacherNameToIdMap.clear();

        while (rs.next()) {
            String tName = rs.getString("name");
            String tId   = rs.getString("id");
            teacherBox.addItem(tName);
            teacherNameToIdMap.put(tName, tId);
        }

        if (teacherBox.getItemCount() > 0) {
            updateSubjectsForSelectedTeacher();
        } else {
            teacherBox.addItem("No teachers in your department");
            JOptionPane.showMessageDialog(this,
                "No teachers found for your department!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading teachers: " + e.getMessage());
    }
    }

    private void updateSubjectsForSelectedTeacher() {
        subjectBox.removeAllItems();
        String selectedTeacher = (String) teacherBox.getSelectedItem();
        if (selectedTeacher == null || selectedTeacher.equals("No teachers available")) return;

        try (Connection con = File_Manager.getConnection()) {
            String sql = "SELECT subjects FROM users WHERE name = ? AND role = 'Teacher'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, selectedTeacher);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String subs = rs.getString("subjects");
                if (subs != null && !subs.isEmpty() && !subs.equals("N/A")) {
                    String[] subjects = subs.split(",");
                    for (String s : subjects) {
                        subjectBox.addItem(s.trim());
                    }
                } else {
                    subjectBox.addItem("General");
                }
            } else {
                subjectBox.addItem("General");
            }
        } catch (Exception e) { 
            e.printStackTrace();
            subjectBox.addItem("General");
        }
    }

    private void setupHistoryTab() {
        JPanel historyContainer = new JPanel(new BorderLayout());
        
        JLabel historyHeader = new JLabel("Your Query History");
        historyHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        historyHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        historyContainer.add(historyHeader, BorderLayout.NORTH);
        
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        historyContainer.add(scroll, BorderLayout.CENTER);
        
        tabs.addTab("Query History", historyContainer);
    }

    private void loadQueryHistory() {
        listPanel.removeAll();
        
        try (Connection con = File_Manager.getConnection()) {
            String sql = "SELECT q.*, u.name as teacher_name " +
                         "FROM student_queries q " +
                         "LEFT JOIN users u ON q.teacher_id = u.id " +
                         "WHERE q.student_id = ? " +
                         "ORDER BY q.query_id DESC";
                         
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentStudentId);
            ResultSet rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                String teacher = rs.getString("teacher_name") != null ? rs.getString("teacher_name") : "Unknown";
                String subject = rs.getString("subject") != null ? rs.getString("subject") : "General";
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                String status = rs.getString("status");
                
                if (answer == null || answer.isEmpty()) {
                    answer = "Waiting for teacher's response...";
                }
                
                listPanel.add(createHistoryCard(teacher, subject, question, answer, status));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                count++;
            }
            
            if (count == 0) {
                JLabel emptyLabel = new JLabel("No queries found. Go to 'New Query' tab to ask a question.");
                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(emptyLabel);
            }
            
        } catch (Exception e) { 
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading history: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            listPanel.add(errorLabel);
        }
        
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createHistoryCard(String teacher, String subject, String question, String answer, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(850, 150));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Teacher: " + teacher + " | Subject: " + subject);
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(new Color(0, 51, 102));
        
        JLabel statusLabel = new JLabel(status.equals("Replied") ? " Replied" : " Pending");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(status.equals("Replied") ? new Color(0, 102, 51) : new Color(255, 102, 0));
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Body Panel
        JPanel bodyPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bodyPanel.setBackground(Color.WHITE);
        
        JLabel questionLabel = new JLabel("<html><b>Question:</b> " + question + "</html>");
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel answerLabel = new JLabel("<html><b>Answer:</b> " + answer + "</html>");
        answerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (!status.equals("Replied")) {
            answerLabel.setForeground(Color.GRAY);
        } else {
            answerLabel.setForeground(new Color(0, 102, 51));
        }
        
        bodyPanel.add(questionLabel);
        bodyPanel.add(answerLabel);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(bodyPanel, BorderLayout.CENTER);
        
        return card;
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