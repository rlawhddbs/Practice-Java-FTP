package Ftp;

import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileTransferProtocolUI extends JFrame {
    public FileTransferProtocolUI() {

        JFrame frame = new JFrame("FTP");
        Container contentPane = frame.getContentPane();
        Font font = new Font("AppleSDGothicNeoBOO", Font.BOLD, 15);
        contentPane.setLayout(null);

        frame.setSize(1000,600);
        frame.setLocationRelativeTo(null);

        JButton upload = new JButton("업로드");
        JButton download = new JButton("다운로드");
        JButton rename = new JButton("파일명 변경");
        JButton delete = new JButton("파일 삭제");
        JButton logout = new JButton("로그아웃");
        JTextField link = new JTextField("");
        JButton browse = new JButton("Browse");
        JTextArea filelist = new JTextArea();
        JTextArea console = new JTextArea();
        JScrollPane fileScrollPane = new JScrollPane(filelist,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JScrollPane consoleScrollPane = new JScrollPane(filelist,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        upload.setBounds(20, 20, 180, 35);
        download.setBounds(210, 20, 180, 35);
        rename.setBounds(400, 20, 180, 35);
        delete.setBounds(590, 20, 180, 35);
        logout.setBounds(780, 20, 180, 35);
        link.setBounds(20, 60, 750, 35);
        browse.setBounds(780, 60, 180, 35);
        filelist.setBounds(20, 100, 940, 300);
        console.setBounds(20, 420, 940, 120);
        fileScrollPane.setBounds(20, 100, 940, 300);
        consoleScrollPane.setBounds(20, 420, 940, 120);

        upload.setFont(font);
        download.setFont(font);
        rename.setFont(font);
        delete.setFont(font);
        logout.setFont(font);
        link.setFont(font);
        browse.setFont(font);
        filelist.setFont(font);
        console.setFont(font);

        contentPane.add(fileScrollPane);
        contentPane.add(consoleScrollPane);
        contentPane.add(upload);
        contentPane.add(download);
        contentPane.add(rename);
        contentPane.add(delete);
        contentPane.add(logout);
        contentPane.add(link);
        contentPane.add(browse);
        contentPane.add(filelist);
        contentPane.add(console);

        fileScrollPane.getVerticalScrollBar().setUnitIncrement(8);
        fileScrollPane.setViewportView(filelist);
        consoleScrollPane.getVerticalScrollBar().setUnitIncrement(8);
        consoleScrollPane.setViewportView(console);

        ActionListener listener = e -> {
            //업로드 버튼을 눌렀을 때
            if (upload.equals(e.getSource())) {
                console.append(FileTransferProtocolServer.Upload(link.getText()));
                try {
                    FTPFile[] files = FileTransferProtocolServer.ftp.listFiles();
                    filelist.setText("");
                    for (FTPFile file : files) {
                        String list = String.format("%.2fmb\t%s\n", (double)file.getSize() / 1024 / 1024, file.getName());
                        filelist.append(list);
                    }
                } catch (IOException ignored) {}
            }
            //다운로드 버튼을 눌렀을 때
            if (download.equals(e.getSource())) {
                console.append(FileTransferProtocolServer.Download(link.getText()));
            }
            //파일명 변경 버튼을 눌렀을 떼
            if (rename.equals(e.getSource())) {
                String renameText = JOptionPane.showInputDialog(null, "", "변경할 파일명을 입력해주세요", JOptionPane.PLAIN_MESSAGE);
                if (renameText != null) {
                    console.append(FileTransferProtocolServer.Rename(link.getText(), renameText));
                    try {
                        FTPFile[] files = FileTransferProtocolServer.ftp.listFiles();
                        filelist.setText("");
                        for (FTPFile file : files) {
                            String list = String.format("%.2fmb\t%s\n", (double)file.getSize() / 1024 / 1024, file.getName());
                            filelist.append(list);
                        }
                    } catch (IOException ignored) {}
                }
            }
            //파일 삭제 버튼을 눌렀을 때
            if (delete.equals(e.getSource())) {
                int result = JOptionPane.showConfirmDialog(null, "정말 진행하시겠습니까?", "파일 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == 0) {
                    console.append(FileTransferProtocolServer.Delete(link.getText()));
                    try {
                        FTPFile[] files = FileTransferProtocolServer.ftp.listFiles();
                        filelist.setText("");
                        for (FTPFile file : files) {
                            String list = String.format("%.2fmb\t%s\n", (double)file.getSize() / 1024 / 1024, file.getName());
                            filelist.append(list);
                        }
                    } catch (IOException ignored) {}
                }
            }
            //browse 버튼을 눌렀을 때
            if (browse.equals(e.getSource())) {
                final JFileChooser jfc = new JFileChooser();
                int returnVal = jfc.showOpenDialog(null);
                if(returnVal == 0) {
                    File file = jfc.getSelectedFile();
                    link.setText(file.getPath());
                    System.out.println("set path : " + file.getPath());
                }
            }
            //로그아웃 버튼을 눌렀을 때
            if (logout.equals(e.getSource())) {
                FileTransferProtocolServer.Logout();
                System.out.println("Logout Successful. Good Bye!");
                frame.setVisible(false);
                new MainUI();
            }
        };

        upload.addActionListener(listener);
        download.addActionListener(listener);
        rename.addActionListener(listener);
        delete.addActionListener(listener);
        logout.addActionListener(listener);
        browse.addActionListener(listener);

        filelist.setEditable(false);
        console.setEditable(false);

        try {
            FTPFile[] files = FileTransferProtocolServer.ftp.listFiles();
            filelist.setText("");
            for (FTPFile file : files) {
                String list = String.format("%.2fmb\t%s\n", (double)file.getSize() / 1024 / 1024, file.getName());
                filelist.append(list);
            }
        } catch (IOException ignored) {}

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}