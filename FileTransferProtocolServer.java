package Ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransferProtocolServer {

    static FTPClient ftp = null;

    static boolean loginSuccess;

    public static void Connect(String host, String user, String pass) {

        ftp = new FTPClient();
        //도메인 접속
        try {
            //ftp 연결 맺기 전에 인코딩을 변경해줘야 적용이 됨
            //connection 연결시에 Encoding 정보를 이용함
            ftp.setControlEncoding("euc-kr");
            //ftp 연결
            ftp.connect(host);

        } catch (IOException e) {
            //예외 코드 출력 및 종료
            System.out.println("IO:"+e.getMessage());
        }

        try {
            //ftp 로그인
            ftp.login(user, pass);
            //로그인이 성공했는지 확인
            loginSuccess = ftp.login(user, pass);

            ftp.enterLocalPassiveMode();

        } catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        }

    }

    private static boolean exists(String fileName, FTPFile[] files) {
        for(FTPFile f : files) {
            if(f.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static String Upload (String path) {
        File uploadFile = new File(path);
        try (final FileInputStream fileInputStream = new FileInputStream(
                uploadFile
        )) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            String fileName = uploadFile.getName();
            FTPFile[] files = ftp.listFiles();
            if (exists(fileName, files)) {
                int result = JOptionPane.showConfirmDialog(null, "덮어쓰시겠습니까?", "파일명이 이미 있습니다", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == 0) {
                    boolean isSuccess = ftp.storeFile(uploadFile.getName(), fileInputStream);
                    if (isSuccess) {
                        //업로드 성공
                        System.out.println("Upload Successful " + ftp.getReplyString());
                        return "Upload Successful " + ftp.getReplyString();
                    } else {
                        //업로드 실패
                        System.out.println("Upload Failed " + ftp.getReplyString());
                        return "Upload Failed " + ftp.getReplyString();
                    }
                }
            } else {
                boolean isSuccess = ftp.storeFile(uploadFile.getName(), fileInputStream);
                if (isSuccess) {
                    //업로드 성공
                    System.out.println("Upload Successful " + ftp.getReplyString());
                    return "Upload Successful " + ftp.getReplyString();
                } else {
                    //업로드 실패
                    System.out.println("Upload Failed " + ftp.getReplyString());
                    return "Upload Failed " + ftp.getReplyString();
                }
            }
        } catch (IOException e) {
            if (!path.equals("")) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }
        return null;
    }

    public static String Download (String path) {
        File downloadFile = new File(path);
        try (final FileOutputStream fileOutputStream = new FileOutputStream(
                String.format("C:/Users/user/Downloads/%s", downloadFile)
        )) {
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean isSuccess = ftp.retrieveFile(downloadFile.getName(), fileOutputStream);
            if (isSuccess) {
                //다운로드 성공
                System.out.println("Download Successful " + ftp.getReplyString());
                return "Download Successful " + ftp.getReplyString();
            } else {
                //다운로드 실패
                System.out.println("Download Failed " + ftp.getReplyString());
                return "Download Failed " + ftp.getReplyString();
            }
        } catch (IOException e) {
            if (!path.equals("")) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }
        return null;
    }

    public static String Rename(String pathFrom, String pathTo) {
        try {
            boolean isSuccess = ftp.rename(pathFrom, pathTo);
            if (isSuccess) {
                //다운로드 성공
                System.out.println("Rename Successful " + ftp.getReplyString());
                return "Rename Successful " + ftp.getReplyString();
            } else {
                //다운로드 실패
                System.out.println("Rename Failed " + ftp.getReplyString());
                return "Rename Failed " + ftp.getReplyString();
            }
        } catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
            return "IO:"+e.getMessage();
        }
    }

    public static String Delete(String path) {
        try {
            boolean isSuccess = ftp.deleteFile(path);
            if (isSuccess) {
                //파일명 변경 성공
                System.out.println("Delete Successful " + ftp.getReplyString());
                return "Delete Successful " + ftp.getReplyString();
            } else {
                //파일명 변경 실패
                System.out.println("Delete Failed " + ftp.getReplyString());
                return "Delete Failed " + ftp.getReplyString();
            }
        } catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
            return "IO:"+e.getMessage();
        }
    }

    public static void Logout () {
        try {
            //로그아웃
            ftp.logout();

        } catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
    }

}
