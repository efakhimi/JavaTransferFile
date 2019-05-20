/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferfiledemo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author 100111010111001
 */
public class Client {
    
    public static void main(String[] args) throws Exception
    {
        boolean flag = false;
        
        InetAddress local = InetAddress.getLocalHost();
        Socket client = new Socket(local, 2030);
        
        InputStream inp = client.getInputStream();
        BufferedReader response = new BufferedReader(new InputStreamReader(inp));
        DataOutputStream request = new DataOutputStream(client.getOutputStream());
        
        
        File f1=null;
        String fileName = "";
        while(true)
        {
            String res = response.readLine();
            
            System.out.println("Client : server response => "+res);
            while(res.contains("newFile"))
            {
                fileName = res.replace("newFile_", "");
                f1 = new File(res.replace("newFile_", ""));
                PrintWriter p = new PrintWriter(f1);
                p.append("");
                p.close();
                
                request.writeBytes("upload\n");
                System.out.println("Client : created file");
                res = "";
            }
            
            while(res.contains("download"))
            {
                System.out.println("Client : downloading file...");
                int length = Integer.parseInt(response.readLine());
                
                byte[] receivedBytes = new byte[length];
                int rb = 0;
                int current = 0;
                
                rb = inp.read(receivedBytes, 0, length);
                
                //System.out.println(rb);

                FileOutputStream f = new FileOutputStream(f1);
                BufferedOutputStream b = new BufferedOutputStream(f);
                
                b.write(receivedBytes);
                b.flush();
                
                
                flag = true;
                
                request.writeBytes("done\n");
                
                System.out.println("Client : downloaded file");
                
                res = "";
                
                b.close();
                f.close();
            }
            
            if(flag){
                
                break;
            }
        }
        
        client.close();
    }

        
}
