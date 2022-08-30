package Ftp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainUI {

    public MainUI() {

        JFrame frame = new JFrame("FTP");
        Container contentPane = frame.getContentPane();
        Font font = new Font("AppleSDGothicNeoBOO", Font.BOLD, 20);
        contentPane.setLayout(null);

        frame.setSize(1000,600);
        frame.setLocationRelativeTo(null);

        JLabel host_text = new JLabel("호스트(H)");
        JTextField host = new JTextField("localhost");
        JLabel user_text = new JLabel("사용자명(U)");
        JTextField user = new JTextField();
        JLabel pass_text = new JLabel("패스워드(P)");
        JPasswordField pass = new JPasswordField();
        JButton submit = new JButton("접속");

        host_text.setBounds(40, -10, 200, 100);
        host.setBounds(40, 70, 200, 35);
        user_text.setBounds(40, 90, 200, 100);
        user.setBounds(40, 170, 200, 35);
        pass_text.setBounds(40, 190, 200, 100);
        pass.setBounds(40, 270, 200, 35);
        submit.setBounds(400, 450, 200, 40);

        host_text.setFont(font);
        host.setFont(font);
        user_text.setFont(font);
        user.setFont(font);
        pass_text.setFont(font);
        pass.setFont(font);
        submit.setFont(font);

        //submit 버튼을 눌렀을 때 발생하는 이벤트
        ActionListener listener = e -> {
            if(submit.equals(e.getSource())) {
                //JTextfield를 String으로 변환
                String data_host = host.getText();
                String data_user = user.getText();
                String data_pass = String.valueOf(pass.getPassword());
                //ftpClass 안에 있는 Submit 함수 호출
                FileTransferProtocolServer.Connect(data_host, data_user, data_pass);
                if (FileTransferProtocolServer.loginSuccess) {
                    //로그인 성공 시
                    System.out.println("login successful. Welcome " + data_user + "! " + FileTransferProtocolServer.ftp.getReplyString());
                    JOptionPane.showMessageDialog(null, "로그인 성공. 환영합니다 " + data_user + "님!", "정보", JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                    new FileTransferProtocolUI();
                } else {
                    //로그인 실패 시
                    System.out.println("login failed. Error code (" + FileTransferProtocolServer.ftp.getReplyCode() + ")");
                    if (FileTransferProtocolServer.ftp.getReplyCode() == 0) {
                        JOptionPane.showMessageDialog(null, "아마 호스트 문제인것 같네요! 오류 코드 : " + FileTransferProtocolServer.ftp.getReplyString() + "\n호스트를 다시 입력 해주세요!", "정보", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패 오류 코드 : " + FileTransferProtocolServer.ftp.getReplyString() + "\n다시 로그인 해주세요!", "정보", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        contentPane.add(host_text);
        contentPane.add(host);
        contentPane.add(user_text);
        contentPane.add(user);
        contentPane.add(pass_text);
        contentPane.add(pass);
        contentPane.add(submit);

        submit.addActionListener(listener);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
