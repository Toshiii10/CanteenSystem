package com.csis;
import javax.swing.*; import java.awt.*; import java.sql.*;
public class UserProfilePanel extends JPanel {
    private final UserSession s;
    private final JTextField name=new JTextField(20),email=new JTextField(20),phone=new JTextField(15),address=new JTextField(20),no=new JTextField(15),dept=new JTextField(20),diet=new JTextField(20),newpass=new JTextField(16),allergens=new JTextField(20);
    private final JComboBox<String> profileSelect = new JComboBox<>(new String[]{"NONE", "DIABETIC_FRIENDLY", "LOW_SODIUM", "VEGETARIAN", "HALAL"});
    public UserProfilePanel(UserSession s){
        super(new GridBagLayout());this.s=s;GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(5,5,5,5);g.fill=GridBagConstraints.HORIZONTAL;
        String[] labels={"Full Name","Email","Phone","Address","Student/Employee No.","Course/Department","Additional Notes","Dietary Restriction Profile","Food Allergy Restrictions (e.g., milk,egg,nuts)","New Password"};
        Component[] fs={name,email,phone,address,no,dept,diet,profileSelect,allergens,newpass};
        for(int i=0;i<labels.length;i++){g.gridx=0;g.gridy=i;add(new JLabel(labels[i]),g);g.gridx=1;add(fs[i],g);}
        JButton save=new JButton("Update Profile"),pass=new JButton("Change Password");g.gridx=0;g.gridy=labels.length;add(save,g);g.gridx=1;add(pass,g);
        save.addActionListener(e->save());pass.addActionListener(e->password());load();
    }
    private void load(){
        try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("SELECT u.full_name,u.email,u.phone,u.address,c.student_employee_no,c.course_department,c.dietary_notes,c.dietary_profile,c.allergen_restrictions FROM users u JOIN customer_profiles c ON c.user_id=u.user_id WHERE u.user_id=?")){
            p.setInt(1,s.userId);ResultSet r=p.executeQuery();
            if(r.next()){
                name.setText(r.getString(1));email.setText(r.getString(2));phone.setText(r.getString(3));address.setText(r.getString(4));
                no.setText(r.getString(5));dept.setText(r.getString(6));diet.setText(r.getString(7));
                profileSelect.setSelectedItem(r.getString(8));allergens.setText(r.getString(9));
            }
        }catch(Exception e){UIUtils.error(this,e);}
    }
    private void save(){
        Connection c=null;
        try{
            c=DB.getConnection();c.setAutoCommit(false);
            PreparedStatement u=c.prepareStatement("UPDATE users SET full_name=?,email=?,phone=?,address=? WHERE user_id=?");
            u.setString(1,name.getText());u.setString(2,email.getText());u.setString(3,phone.getText());u.setString(4,address.getText());u.setInt(5,s.userId);u.executeUpdate();
            PreparedStatement p=c.prepareStatement("UPDATE customer_profiles SET student_employee_no=?,course_department=?,dietary_notes=?,dietary_profile=?,allergen_restrictions=? WHERE user_id=?");
            p.setString(1,no.getText());p.setString(2,dept.getText());p.setString(3,diet.getText());p.setString(4,String.valueOf(profileSelect.getSelectedItem()));p.setString(5,allergens.getText().trim().toLowerCase());p.setInt(6,s.userId);p.executeUpdate();
            c.commit();Audit.log(s,"UPDATE_PROFILE","Updated profile nutrition preferences");UIUtils.info(this,"Profile updated.");
        }catch(Exception e){try{if(c!=null)c.rollback();}catch(Exception ignored){}UIUtils.error(this,e);}finally{try{if(c!=null)c.close();}catch(Exception ignored){}}
    }
    private void password(){
        if(newpass.getText().trim().length()<6){UIUtils.info(this,"Password must be at least 6 characters.");return;}
        try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement("UPDATE users SET password_hash=? WHERE user_id=?")){p.setString(1,PasswordUtil.hash(newpass.getText()));p.setInt(2,s.userId);p.executeUpdate();newpass.setText("");Audit.log(s,"CHANGE_PASSWORD","Password changed");UIUtils.info(this,"Password changed.");}catch(Exception e){UIUtils.error(this,e);}
    }
}