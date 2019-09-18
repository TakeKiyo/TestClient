package sample;

import com.sun.prism.image.CachingCompoundImage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public Button start;
    public Button send;
    public TextField ip_ad;
    public TextField port_num;
    public Label connect;
    public TextField output;
    public TextArea input;
    Socket socket = null;
    public List<String> bcc_byte = new ArrayList<String>();
    public String[] bcc_string = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    public void Clicked(ActionEvent event) {
        String result = "接続中";
        connect.setText(result);
        String ip = ip_ad.getText();
        int port = Integer.parseInt(port_num.getText());
        System.out.println(ip);
        System.out.println(port);
        bcc_byte.add("0000");
        bcc_byte.add("0001");
        bcc_byte.add("0010");
        bcc_byte.add("0011");
        bcc_byte.add("0100");
        bcc_byte.add("0101");
        bcc_byte.add("0110");
        bcc_byte.add("0111");
        bcc_byte.add("1000");
        bcc_byte.add("1001");
        bcc_byte.add("1010");
        bcc_byte.add("1011");
        bcc_byte.add("1100");
        bcc_byte.add("1101");
        bcc_byte.add("1110");
        bcc_byte.add("1111");
        try {
            socket = new Socket(ip, port);
            InThread inThread = new InThread(socket);
            inThread.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public String make_bcc(String data){
        byte[] data_byte = data.getBytes();
        byte res = (byte) (data_byte[0] ^ data_byte[1]);
        for (int i=2;i<data_byte.length;i++){
            res = (byte)(res ^ data_byte[i]);
        }

        String str = Integer.toBinaryString(res);
        str = String.format("%8s", str).replace(' ', '0');
        String first = str.substring(0,4);
        String second = str.substring(4,8);
        int first_idx = bcc_byte.indexOf(first);
        int second_idx = bcc_byte.indexOf(second);
        first = bcc_string[first_idx];
        second = bcc_string[second_idx];
        return first+second;

    }
    class InThread extends Thread{
        private Socket socket;
        private InputStream in;
        public int totalBytesRcvd ;
        public int bytesRcvd;
        public InThread(Socket socket){
            this.socket = socket;
            try{
                this.in = socket.getInputStream();
                GetThread getThread = new GetThread(in);
                getThread.start();
//                while (totalBytesRcvd < 2) {
//                    if ((bytesRcvd = in.read( // 引数は読込データ、Offset、読込データ長
//                            msg,
//                            totalBytesRcvd,
//                            2 - totalBytesRcvd)) == -1) {
//                        break;
//                    }
//                    totalBytesRcvd += bytesRcvd;
//                }
            }catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    class GetThread extends Thread{
        private InputStream in;
        public GetThread(InputStream inputStream){
            this.in = inputStream;
        }
        public void run(){
            try{
                int totalBytesRcvd;
                byte[] bytenum;
                input.setText("start");
                while(true) {
//                    bytenum = new byte[1];
//                    in.read(bytenum, 0, 1);
//                    String aa = new String(bytenum);
//                    int num = Integer.parseInt(aa);
//                    System.out.println(num);
//                    byte[] receiveBuf = new byte[num];
//                    totalBytesRcvd = 0;
//                    int recvMsgSize;
//                    System.out.println("OK");
//                    while (true) {
//                        recvMsgSize = in.read(receiveBuf);
//                        totalBytesRcvd += recvMsgSize;
//                        if (totalBytesRcvd == num) {
//                            break;
//                        }
//                    }
                    byte[] msg = new byte[1024];
                    in.read(msg);

//                    String m = new String(receiveBuf);
                    String m = new String(msg);
                    String txt = input.getText();
                    input.setText(txt + "\n" + m);
                    System.out.println(m);
                }
            }catch (IOException e) {
                System.out.println(e);
            }


        }
    }

    public void sendClicked(ActionEvent event) {
        try{
            OutputStream out = socket.getOutputStream();
            String sData = output.getText();
            byte[] first = new byte[1];
            first[0] = 0x02;
            String fi_st = new String(first);

            String bcc = make_bcc(sData);
            byte[] last = new byte[1];
            last[0] = 0x03;
            String la_st = new String(last);



            String data_st = fi_st + sData +bcc+ la_st;
            System.out.println(data_st);
            byte[] data = data_st.getBytes();
            out.write(data);
            System.out.println("sent");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void Finish(ActionEvent event) {
        System.exit(0);
    }


}
