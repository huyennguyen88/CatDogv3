package catDogNN;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

public class View extends JPanel implements MouseListener 
{
    private JFrame mainFrame;
    private JPanel mainPanel;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private ImagePanel sourceImagePanel;
    private JLabel predictionResponse;
    private File selectedFile;
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
	public View()
	{

			initUI();
	}
	 public void initUI() {

	        // create main frame
	        mainFrame = createMainFrame();

	        mainPanel = new JPanel();
	        mainPanel.setLayout(new GridBagLayout());

	        JButton chooseButton = new JButton("Choose Pet Image");
	        chooseButton.addActionListener(e -> {
	            chooseFileAction();
	            predictionResponse.setText("");
	        });

	        JButton predictButton = new JButton("Is it Cat or a Dog?");
	        predictButton.addActionListener(e -> {
	                PetType petType = new PetType(selectedFile);
	                if (petType.Predict()) {
	                    predictionResponse.setText("It is a Cat");
	                    predictionResponse.setForeground(Color.GREEN);
	                } 
	                else {
	                    predictionResponse.setText("It is a Dog");
	                    predictionResponse.setForeground(Color.RED);
	                }
	                mainPanel.updateUI();
	        });

	        try {
				fillMainPanel(chooseButton, predictButton);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        mainFrame.add(mainPanel, BorderLayout.CENTER);
	        mainFrame.setVisible(true);

	    }

	    private void fillMainPanel(JButton chooseButton, JButton predictButton) throws IOException {
	        GridBagConstraints c = new GridBagConstraints();
	        c.gridx = 1;
	        c.gridy = 1;
	        c.weighty = 0;
	        c.weightx = 0;
	        JPanel buttonsPanel = new JPanel(new FlowLayout());
	        buttonsPanel.add(chooseButton);
	        buttonsPanel.add(predictButton);
	        mainPanel.add(buttonsPanel, c);

	        c.gridx = 1;
	        c.gridy = 2;
	        c.weighty = 1;
	        c.weightx = 1;
	        sourceImagePanel = new ImagePanel();
	        mainPanel.add(sourceImagePanel, c);

	        c.gridx = 1;
	        c.gridy = 3;
	        c.weighty = 0;
	        c.weightx = 0;
	        predictionResponse = new JLabel();
	        predictionResponse.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 72));
	        mainPanel.add(predictionResponse, c);
	    }


	    public void chooseFileAction() {
	        JFileChooser chooser = new JFileChooser("D:\\DoAn2\\dogs-vs-cats\\test1\\test1");
	        chooser.setCurrentDirectory(new File(new File("resources").getAbsolutePath()));
	        int action = chooser.showOpenDialog(null);
	        if (action == JFileChooser.APPROVE_OPTION) {
	            try {
	                selectedFile = chooser.getSelectedFile();
	                showSelectedImageOnPanel(new FileInputStream(selectedFile), sourceImagePanel);
	            } catch (IOException e1) {
	                throw new RuntimeException(e1);
	            }
	        }
	    }

	    private void showSelectedImageOnPanel(InputStream selectedFile, ImagePanel imagePanel) throws IOException {
	        BufferedImage bufferedImage = ImageIO.read(selectedFile);
	        imagePanel.setImage(bufferedImage);
	    }


	    private JFrame createMainFrame() {
	        JFrame mainFrame = new JFrame();
	        mainFrame.setTitle("Image Recognizer");
	        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	        mainFrame.setLocationRelativeTo(null);
	        mainFrame.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                System.exit(0);
	            }
	        });
	        ImageIcon imageIcon = new ImageIcon("icon.png");
	        mainFrame.setIconImage(imageIcon.getImage());

	        return mainFrame;
	    }

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
    @Override
	public void mouseExited(MouseEvent e) {}
	public static void main(String[] args) 
	{
	     try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
	        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
			new View();
		} catch (Exception e) {

			e.printStackTrace();
		} 
	        


	}




}
