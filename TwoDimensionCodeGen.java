//package qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;


import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

public class TwoDimensionCodeGen extends JFrame{
  
  private String contentStr="";
  private String imagePath="";
  
  JFrame window;
  JTextField contentField;
  JTextField imagepathField;
  JButton selectpathBtn;
  JButton genBtn;
  JLabel imageLabel;
  JLabel filepathLabel;
  private JFileChooser chooser;
  
  public TwoDimensionCodeGen(){
    window=new JFrame("二维码生成 V1.0");
    window.setLayout(null);
    window.setSize(550, 500);           //设置大小
    window.setLocationRelativeTo(null); //设置居中
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置可关闭
    window.setLayout(null);             //设置绝对布局（窗口里面的内容不会随着窗口的改变而改变）
    window.setResizable(false);         //设置窗口不可拉伸改变大小
    //设置内容标签
    JLabel content_label =new JLabel("内容:");
    content_label.setBounds(20,20,50,30);
    window.add(content_label);
    //设置二维码内容
    contentField=new JTextField();
    contentField.setBounds(70, 18, 350, 80);
    window.add(contentField);
    
    //设置图片输出目录
    JLabel imagepath_label =new JLabel("输出:");
    imagepath_label.setBounds(20,120,50,30);
    window.add(imagepath_label);
    
    //设置图片保存目录
    imagepathField=new JTextField();//隐藏密码
    imagepathField.setBounds(70, 118, 350, 30);
    window.add(imagepathField);
    //选择图片保存路径按钮
    selectpathBtn=new JButton("选择");
    selectpathBtn.setBounds(425, 118, 80, 30);
    window.add(selectpathBtn);
    selectpathBtn.addActionListener(new ActionListener(){  //监听选择路径按钮的点击事件
      public void actionPerformed(ActionEvent evt){ 
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();    //注意了，这里重要的一句
        System.out.println(fsv.getHomeDirectory());                 //得到桌面路径
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择要上传的文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
          path=fileChooser.getSelectedFile().getPath();
          System.out.println("path: "+path);
          imagepathField.setText(path);
        }
      }
    });
    //生成按钮
    genBtn=new JButton("生成");
    genBtn.setBounds(425, 155, 80, 30);
    window.add(genBtn);
    //二维图显示标签
    imageLabel=new JLabel();
    imageLabel.setBounds(180,160,200,200);
    window.add(imageLabel);
    //二维图文件完整路径显示标签
    filepathLabel=new JLabel("");
    filepathLabel.setBounds(20,370,510,30);
    window.add(filepathLabel);
    
    genBtn.addActionListener(new ActionListener(){             //监听生成二维图按钮的点击事件
      public void actionPerformed(ActionEvent evt){
        //TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContent=contentField.getText();
        String imgPath=imagepathField.getText();
        if(encoderContent==null){
        encoderContent="";
        }
        encoderContent=encoderContent.trim();
        if(encoderContent.equals("")){
          JOptionPane.showMessageDialog(null, "请填写内容", "提示 ", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if(imgPath==null){
          imgPath=".\\images\\";
        }
        imgPath=imgPath.trim();
        if(imgPath.equals("")){
          imgPath=System.getProperty("user.dir")+"\\images\\";
        }
        if(!imgPath.endsWith("\\")){
          imgPath=imgPath+"\\";
        }
        if(imagepathField.getText().equals("")){
          imagepathField.setText(imgPath);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        imgPath=imgPath+sdf.format(new java.util.Date())+".png";
        encoderQRCode(encoderContent, imgPath, "png");
        imageLabel.setIcon(new ImageIcon(imgPath));
        filepathLabel.setText(imgPath);
      }
    });
    window.setVisible(true);//设置面板可见
  }
  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param imgPath 图片路径
   */
  public void encoderQRCode(String content, String imgPath) {
    this.encoderQRCode(content, imgPath, "png", 7);
  }
  
  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param output 输出流
   */
  public void encoderQRCode(String content, OutputStream output) {
    this.encoderQRCode(content, output, "png", 7);
  }
  
  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param imgPath 图片路径
   * @param imgType 图片类型
   */
  public void encoderQRCode(String content, String imgPath, String imgType) {
    this.encoderQRCode(content, imgPath, imgType, 7);
  }
  
  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param output 输出流
   * @param imgType 图片类型
   */
  public void encoderQRCode(String content, OutputStream output, String imgType) {
    this.encoderQRCode(content, output, imgType, 7);
  }

  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param imgPath 图片路径
   * @param imgType 图片类型
   * @param size 二维码尺寸
   */
  public void encoderQRCode(String content, String imgPath, String imgType, int size) {
    try {
      BufferedImage bufImg = this.qRCodeCommon(content, imgType, size);
      
      File imgFile = new File(imgPath);
      // 生成二维码QRCode图片
      ImageIO.write(bufImg, imgType, imgFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 生成二维码(QRCode)图片
   * @param content 存储内容
   * @param output 输出流
   * @param imgType 图片类型
   * @param size 二维码尺寸
   */
  public void encoderQRCode(String content, OutputStream output, String imgType, int size) {
    try {
      BufferedImage bufImg = this.qRCodeCommon(content, imgType, size);
      // 生成二维码QRCode图片
      ImageIO.write(bufImg, imgType, output);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 生成二维码(QRCode)图片的公共方法
   * @param content 存储内容
   * @param imgType 图片类型
   * @param size 二维码尺寸
   * @return
   */
  private BufferedImage qRCodeCommon(String content, String imgType, int size) {
    BufferedImage bufImg = null;
    try {
      Qrcode qrcodeHandler = new Qrcode();
      // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，
      //但对二维码清晰度的要求越小
      qrcodeHandler.setQrcodeErrorCorrect('M');
      qrcodeHandler.setQrcodeEncodeMode('B');
      // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
      qrcodeHandler.setQrcodeVersion(size);
      // 获得内容的字节数组，设置编码格式
      byte[] contentBytes = content.getBytes("utf-8");
      // 图片尺寸
      int imgSize = 67 + 12 * (size - 1);
      bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
      Graphics2D gs = bufImg.createGraphics();
      // 设置背景颜色
      gs.setBackground(Color.WHITE);
      gs.clearRect(0, 0, imgSize, imgSize);

      // 设定图像颜色> BLACK
      gs.setColor(Color.BLACK);
      // 设置偏移量，不设置可能导致解析出错
      int pixoff = 2;
      // 输出内容> 二维码
      if (contentBytes.length > 0 && contentBytes.length < 800) {
        boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
        for (int i = 0; i < codeOut.length; i++) {
          for (int j = 0; j < codeOut.length; j++) {
            if (codeOut[j][i]) {
              gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
            }
          }
        }
      } else {
        throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
      }
      gs.dispose();
      bufImg.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bufImg;
  }
  
  /**
   * 解析二维码（QRCode）
   * @param imgPath 图片路径
   * @return
   */
  public String decoderQRCode(String imgPath) {
    // QRCode 二维码图片的文件
    File imageFile = new File(imgPath);
    BufferedImage bufImg = null;
    String content = null;
    try {
      bufImg = ImageIO.read(imageFile);
      QRCodeDecoder decoder = new QRCodeDecoder();
      content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8"); 
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    } catch (DecodingFailedException dfe) {
      System.out.println("Error: " + dfe.getMessage());
      dfe.printStackTrace();
    }
    return content;
  }
  
  /**
   * 解析二维码（QRCode）
   * @param input 输入流
   * @return
   */
  public String decoderQRCode(InputStream input) {
    BufferedImage bufImg = null;
    String content = null;
    try {
      bufImg = ImageIO.read(input);
      QRCodeDecoder decoder = new QRCodeDecoder();
      content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8"); 
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    } catch (DecodingFailedException dfe) {
      System.out.println("Error: " + dfe.getMessage());
      dfe.printStackTrace();
    }
    return content;
  }

  public static void main(String[] args) {
    TwoDimensionCodeGen tdc=new TwoDimensionCodeGen();
   /*
    String imgPath = "D:/Michael_QRCode.png";
    String encoderContent = "Hello 大大、小小,welcome to QRCode!" + "\nMyblog [ http://sjsky.iteye.com ]" + "\nEMail [ sjsky007@gmail.com ]";
    if(args.length>=2){
      imgPath=args[0];
      encoderContent=args[1];
    }
    */
    //try {
    //  OutputStream output = new FileOutputStream(imgPath);
    //  handler.encoderQRCode(content, output);
    //} catch (Exception e) {
    //  e.printStackTrace();
    //}
    System.out.println("========encoder success");
    //String decoderContent = handler.decoderQRCode(imgPath);
    //System.out.println("解析结果如下：");
    //System.out.println(decoderContent);
    //System.out.println("========decoder success!!!");
  }
}

