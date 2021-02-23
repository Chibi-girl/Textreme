package application;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

class SimpleTextEditor implements ActionListener
{
	JFrame frame,ftable;JTextPane txtarea;JScrollPane scroll;JTable table;
	JMenuBar menu;JMenu file,edit,exit,view,preferences;Icon lefticon,righticon,centreicon,justifiedicon;
	JMenuItem newfile,openfile,savefile,undo,redo,saveas,toggle,font,shortcut,copy,cut,paste,view_panel,hide_panel,close,printfile;
	JPanel panel,controlPanel,buttonpanel;JButton bold,italics,underline,left,right,centre,justified,ok,ok2,cancel;JOptionPane dopen,dquit;
	JLabel sampleText;JDialog dialog;JComboBox fontComboBox,sizeComboBox;JCheckBox boldCheck, italCheck;
	String filename=null;String[] fonts;String column[]={"Action","Keyboard shortcut"}; String name;
	Integer size,b=0,i=0,u=0;
	String data[][]={ {"New File","Ctrl+N"},{"Open File","Ctrl+O"},{"Print File","Ctrl+P"},{"Save New File","Ctrl+Shift+S"},
	{"Save File","Ctrl+S"},{"Cut","Ctrl+X"},{"Copy","Ctrl+C"},{"Paste","Ctrl+V"},{"Undo","Ctrl+Z"},{"Redo","Ctrl+Y"},
	{"View Bottom Panel","Ctrl+Shift+V"},{"Hide Bottom Panel","Ctrl+Shift+H"},{"Toggle between light/dark mode","Ctrl+T"},
	{"Change text style","Ctrl+F"},{"Exit","Ctrl+Q"},{"Bold","Ctrl+B"},{"Italics","Ctrl+I"},{"Underline","Ctrl+U"}}; 
	Integer[] sizes = { 7, 8, 9, 10, 11, 12, 14, 18, 20, 22, 24, 36 };
	UndoManager undomanager=new UndoManager();FontListener fl = new FontListener();Font f;
	SimpleAttributeSet attribs = new SimpleAttributeSet();
	
	SimpleTextEditor()
	{
		try
		{
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			 
		}
		catch(Exception e) { }
	frame= new JFrame("Untitled Document");
	frame.setBounds(100,100,616,444);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ftable=new JFrame("List of Keyboard Shortcuts");
	ftable.setBounds(100,100,530,395);   
	ftable.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	txtarea= new JTextPane();
	txtarea.setFont(new Font("Chilanka Regular", Font.PLAIN, 20));
	txtarea.setBackground(Color.WHITE);
	txtarea.setForeground(Color.MAGENTA);
	txtarea.setBounds(200,100,200,100);
	frame.getContentPane().add(txtarea,BorderLayout.NORTH);
	
	scroll= new JScrollPane(txtarea); 
	frame.getContentPane().add(scroll,BorderLayout.CENTER);
	
	panel=new JPanel();
	panel.setBackground(Color.PINK);
	panel.setSize(100,100);
	frame.getContentPane().add(panel,BorderLayout.SOUTH);
	
	menu=new JMenuBar();
	menu.setMargin(new Insets(5,10,5,10));
	menu.setBackground(new java.awt.Color(120,180,255));
	frame.getContentPane().add(menu,BorderLayout.NORTH);
	
	file=new JMenu("File");edit=new JMenu("Edit");view=new JMenu("Bottom Panel");
	preferences=new JMenu("Preferences");
	menu.add(file);menu.add(new JMenu("|")).setEnabled(false);
	menu.add(edit);menu.add(new JMenu("|")).setEnabled(false);
	menu.add(preferences);menu.add(new JMenu ("|")).setEnabled(false);

	newfile=new JMenuItem("New");openfile=new JMenuItem("Open");savefile=new JMenuItem("Save");
	saveas=new JMenuItem("SaveAs");printfile=new JMenuItem("Print");
	view_panel=new JMenuItem("View");hide_panel=new JMenuItem("Hide");shortcut=new JMenuItem("Keyboard Shortcuts");
	copy=new JMenuItem("Copy");paste=new JMenuItem("Paste");close=new JMenuItem("Exit");
	cut=new JMenuItem("Cut");toggle=new JMenuItem("Toggle view mode");font=new JMenuItem("Fonts");
	undo=new JMenuItem("Undo");redo=new JMenuItem("Redo");ok= new JButton("Change selected"); cancel=new JButton ("Cancel");ok2=new JButton("Change entire document");
	bold=new JButton("Bold");italics=new JButton("Italics");underline=new JButton("Underline");
	righticon=new ImageIcon("right.png");right=new JButton(righticon);
	lefticon=new ImageIcon("left.png");left=new JButton(lefticon);
	centreicon=new ImageIcon("centre.png");centre=new JButton(centreicon);justified=new JButton("Justified");
	
	bold.setFont(new Font("Serif", Font.BOLD, 18));
	italics.setFont(new Font("Serif", Font.ITALIC, 18));
	underline.setFont(new Font("Serif", Font.PLAIN, 18));
	Font uf = underline.getFont();
	Map attributes = uf.getAttributes();
	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	underline.setFont(uf.deriveFont(attributes));
	
	
	newfile.addActionListener(this);openfile.addActionListener(this);printfile.addActionListener(this);
	savefile.addActionListener(this);saveas.addActionListener(this);
	copy.addActionListener(this);paste.addActionListener(this);cut.addActionListener(this);
	view_panel.addActionListener(this);hide_panel.addActionListener(this);shortcut.addActionListener(this);
	undo.addActionListener(this);redo.addActionListener(this); 
	close.addActionListener(this);toggle.addActionListener(this);font.addActionListener(this);
	bold.addActionListener(new StyledEditorKit.BoldAction());italics.addActionListener(new StyledEditorKit.ItalicAction());
	underline.addActionListener(new StyledEditorKit.UnderlineAction());
	left.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			StyledDocument doc = txtarea.getStyledDocument();
        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_LEFT);  
        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
		}
	});
	right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				StyledDocument doc = txtarea.getStyledDocument();
	        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_RIGHT);  
	        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
			}
		});
	centre.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			StyledDocument doc = txtarea.getStyledDocument();
        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);  
        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
		}
	});
	justified.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			StyledDocument doc = txtarea.getStyledDocument();
        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_JUSTIFIED);  
        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
		}
	});
	
	menu.add(close);
	file.add(newfile);file.addSeparator();file.add(openfile);
	file.addSeparator();file.add(printfile);file.addSeparator();file.add(savefile);
	file.addSeparator();view.add(view_panel);view.addSeparator();view.add(hide_panel);
	preferences.add(view);preferences.addSeparator();file.add(saveas);edit.add(cut);edit.addSeparator();
	edit.add(copy);edit.addSeparator();edit.add(paste);edit.addSeparator();edit.add(undo);
	edit.addSeparator();edit.add(redo);preferences.add(toggle);preferences.addSeparator();
	preferences.add(font);preferences.addSeparator();preferences.add(shortcut);
	panel.add(bold);panel.add(italics);panel.add(underline);
	panel.add(left);panel.add(centre);panel.add(right);
	
	table=new JTable(data,column)  
	{
		public boolean editCellAt(int row, int column, java.util.EventObject e) 
		{
            return false;
        }
	};
    table.setBounds(100,100,500,70);          
    JScrollPane sp=new JScrollPane(table);  
    ftable.add(sp); 
	
	sampleText= new JLabel("      Sample Text");
	dialog = new JDialog(frame,"Select Font");
	dialog.add(sampleText, BorderLayout.CENTER);      

	GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
	fonts = g.getAvailableFontFamilyNames();

	controlPanel = new JPanel(); buttonpanel=new JPanel();
	fontComboBox = new JComboBox(fonts);sizeComboBox = new JComboBox(sizes);
	boldCheck = new JCheckBox("Bold");italCheck = new JCheckBox("Italics");
	
	
	boldCheck.addActionListener(fl);fontComboBox.addActionListener(fl);ok.addActionListener(this);ok2.addActionListener(this);
			
	italCheck.addActionListener(fl);sizeComboBox.addActionListener(fl);cancel.addActionListener(this);
	
	
	controlPanel.add(new JLabel("Family: "));controlPanel.add(new JLabel("Size: "));
    sizeComboBox.setSelectedIndex(5);
    
    controlPanel.add(fontComboBox);controlPanel.add(sizeComboBox);buttonpanel.add(ok);//buttonpanel.add(ok2);
    controlPanel.add(boldCheck);controlPanel.add(italCheck);buttonpanel.add(cancel);

    dialog.add(controlPanel, BorderLayout.SOUTH);
    dialog.add(buttonpanel,BorderLayout.NORTH);
    
    txtarea.getDocument().addUndoableEditListener(new UndoableEditListener() {
		public void undoableEditHappened(UndoableEditEvent e) {
			undomanager.addEdit(e.getEdit());
		}
	});
    txtarea.getActionMap().put("New",new AbstractAction("New") {
		public void actionPerformed(ActionEvent e)
		{
			newFile();
		}
	}); 
    txtarea.getActionMap().put("Open",new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e)
		{
			openFile();
		}
	}); 
    txtarea.getActionMap().put("Print",new AbstractAction("Print") {
		public void actionPerformed(ActionEvent e)
		{
			print();
		}
	}); 
    txtarea.getActionMap().put("SaveAs",new AbstractAction("SaveAs") {
		public void actionPerformed(ActionEvent e)
		{
			saveFile(null);
		}
	}); 
    txtarea.getActionMap().put("Save",new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e)
		{
			saveFile(filename);
		}
	}); 
	txtarea.getActionMap().put("Undo",new AbstractAction("Undo") {
        public void actionPerformed(ActionEvent e) {
     	   undo();
        }
   });
	txtarea.getActionMap().put("Redo",new AbstractAction("Redo") {
        public void actionPerformed(ActionEvent e) {
            redo();
        }
    });
	txtarea.getActionMap().put("View",new AbstractAction("View") {
        public void actionPerformed(ActionEvent e) {
            panel.setVisible(true);
        }
    });
	txtarea.getActionMap().put("Hide",new AbstractAction("Hide") {
        public void actionPerformed(ActionEvent e) {
            panel.setVisible(false);
        }
    });
	txtarea.getActionMap().put("Toggle",new AbstractAction("Toggle") {
        public void actionPerformed(ActionEvent e) {
            toggle();
        }
    });
	txtarea.getActionMap().put("Fonts",new AbstractAction("Fonts") {
        public void actionPerformed(ActionEvent e) {
            fonts();
        }
    });
	txtarea.getActionMap().put("Exit",new AbstractAction("Exit") {
        public void actionPerformed(ActionEvent e) {
            exit();
        }
    });
	txtarea.getActionMap().put("Bold", new StyledEditorKit.BoldAction());	
	txtarea.getActionMap().put("Italics", new StyledEditorKit.ItalicAction());
	txtarea.getActionMap().put("Underline", new StyledEditorKit.UnderlineAction());
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N"), "New");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), "Open");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control P"), "Print");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift control S"), "SaveAs");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "Save");
	//txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control X"), "Cut");
	//txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "Copy");
	//txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "Paste");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), "Undo");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), "Redo");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control shift V"), "View");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control shift H"), "Hide");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control T"), "Toggle");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control F"), "Fonts");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Q"), "Exit");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control B"), "Bold");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control I"), "Italics");
	txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control U"), "Underline");
	}
	
	 public void actionPerformed(ActionEvent e) 
	 { 
	        String s = e.getActionCommand(); 
	        
	        if (s.equals("Cut")) { 
	            txtarea.cut(); 
	        } 
	        else if (s.equals("Copy")) { 
	            txtarea.copy(); 
	        } 
	        else if (s.equals("Paste")) { 
	            txtarea.paste(); 
	        } 
	        else if (s.contentEquals("Undo")) {
	        	undo();
	        }
	        else if (s.contentEquals("Redo")) {
	        	redo();
	        }
	        else if (s.equals("Toggle view mode")) {
	        	toggle();
	        }
	        else if (s.contentEquals("Fonts")) {
	        	 fonts();
	        }
	        else if (s.contentEquals("Change selected"))
	        {
	        	new StyledEditorKit.FontSizeAction("Change",size).actionPerformed(e);
	        	new StyledEditorKit.FontFamilyAction("Change", name).actionPerformed(e);
	        	if(b==1)
	        		new StyledEditorKit.BoldAction().actionPerformed(e);
	        	if(i==1)
	        		new StyledEditorKit.ItalicAction().actionPerformed(e);
	        	dialog.setVisible(false);
	        }
	        /*else if (s.contentEquals("Change entire document")){
	        	txtarea.setFont(f);
	        	dialog.setVisible(false);
	        }*/
	        else if (s.contentEquals("Cancel")) {
	        	dialog.setVisible(false);
	        }
	        else if (s.equals(lefticon)) {
	        	
	        }
	        else if (s.equals(centreicon)) {
	        	StyledDocument doc = txtarea.getStyledDocument();
	        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);  
	        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
	        }
	        else if (s.contentEquals("Right")) {
	        	StyledDocument doc = txtarea.getStyledDocument();
	        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_RIGHT);  
	        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
	        }
	        else if (s.contentEquals("Justified")) {
	        	StyledDocument doc = txtarea.getStyledDocument();
	        	StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_JUSTIFIED);  
	        	doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
	        }
	        else if (s.contentEquals("Keyboard Shortcuts")) {
	        	ftable.setVisible(true);
	        }
	        else if (s.equals("Exit")) { 
	        	exit();
	        } 
	        else if (s.equals("New")) {
	        	 newFile();
	        }
	        else if (s.equals("View")) {
	        	panel.setVisible(true);
	        }
	        else if (s.equals("Hide")) {
        		panel.setVisible(false);
            }
	        else if (s.equals("Open")) {  
	            openFile();
	        } 
	        else if (s.equals("SaveAs")) { 
	            saveFile(null); 
	        } 
	        else if(s.equals("Save")) {
            	saveFile(filename);
            }
	        else if (s.equals("Print")) { 
	            print();
	        } 
	        
	  }
	 private void undo()
	 {
		 try {
             if (undomanager.canUndo()) {
                 undomanager.undo();
             }
         } catch (CannotUndoException ex) {
         }
	 }
	 private void redo()
	 {
		 try {
             if (undomanager.canRedo()) {
                 undomanager.redo();
             }
         } catch (CannotRedoException ex) {
         }
	 }
	 private void fonts()
	 {
		 dialog.setSize(620, 200);
    	 dialog.setLocationRelativeTo(null);
    	 dialog.setVisible(true);
	 }
	 private void toggle()
	 {
		 if(txtarea.getBackground()==Color.WHITE)
     	{
     		txtarea.setBackground(Color.DARK_GRAY);
     		txtarea.setForeground(Color.CYAN);
     		panel.setBackground(Color.LIGHT_GRAY);
     		menu.setBackground(Color.LIGHT_GRAY);
     	}
     	else
     	{
     		txtarea.setBackground(Color.WHITE);
     		txtarea.setForeground(Color.MAGENTA);
     		panel.setBackground(Color.PINK);
     		menu.setBackground(new java.awt.Color(120,180,255));
     	}
	 }
	 private void newFile() {
		 Object stringArray[] = { "Sure", "No. I'd like to change/save" };
    	 int response=JOptionPane.showOptionDialog(frame, "You're about to delete this text. Continue?", "Select an Option",
    	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, stringArray,stringArray[0]);
    
    	 if (response == JOptionPane.YES_OPTION) {
    		 txtarea.setText(""); 
    		 filename=null;
    		 frame.setTitle("Untitled Document");
    	    } 
	 }
	 private void openFile()
	 {
		 JFileChooser j = new JFileChooser("/home/aishwarya"); 
         int r = j.showOpenDialog(null); 
         // If the user selects a file 
         if (r == JFileChooser.APPROVE_OPTION) { 
             // Set the label to the path of the selected directory 
         	
             filename=j.getSelectedFile().getAbsolutePath();
             String name=j.getSelectedFile().getName();
             File fi = new File(filename); 
             try { 
                 String s1 = "", sl = ""; 
                 BufferedReader br = new BufferedReader(new FileReader(fi)); 
                 sl = br.readLine(); 
                 while ((s1 = br.readLine()) != null) { 
                     sl = sl + "\n" + s1; 
                 } 
                 
                 //StyledDocument doc1 = fi.getStyledDocument();
             	//StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_JUSTIFIED);  
             	//doc.setParagraphAttributes(0, doc.getLength(), attribs, false);
                 
                 txtarea.setText(sl); 
                 frame.setTitle(name);
             } 
             catch (Exception evt) { 
                 JOptionPane.showMessageDialog(frame, evt.getMessage()); 
             } 
         }
	 }
	 private void print() {
		 try { 
             txtarea.print(); 
         } 
         catch (Exception evt) { 
             JOptionPane.showMessageDialog(frame, evt.getMessage()); 
         } 
	 }
	 private void saveFile(String name) 
	 {
		    if (name == null) {  
		    	// get filename from user
		      JFileChooser fc = new JFileChooser();
		      if (fc.showSaveDialog(null) != JFileChooser.CANCEL_OPTION)
		        name = fc.getSelectedFile().getAbsolutePath();
		      String setname=fc.getSelectedFile().getName();
		      	frame.setTitle(setname);
		    }
		    if (name != null) {  
		      try {
		        Formatter out = new Formatter(new File(name));  
		        filename = name;
		        out.format("%s", txtarea.getText());
		        out.close();
		        JOptionPane.showMessageDialog(null, "Saved to " + filename,
		          "Save File", JOptionPane.PLAIN_MESSAGE);
		      }
		      catch (FileNotFoundException e) {
		        JOptionPane.showMessageDialog(null, "Cannot write to file: " + name,
		          "Error", JOptionPane.ERROR_MESSAGE);
		      }
		    }
	 }
	 private void exit() {
		 Object stringArray[] = { "Sure", "No. I'd like to change/save" };
    	 int response=JOptionPane.showOptionDialog(frame, "You're about to exit. Continue?", "Select an Option",
    	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, stringArray,stringArray[0]);
    
    	 if (response == JOptionPane.YES_OPTION) {
    		 frame.setVisible(false);
    	    } 
	 }
	 
	 
	 private class FontListener implements ActionListener {
		    public void actionPerformed(ActionEvent e) {
		      updateText();
		    }
	 
	 
	 public void updateText() 
	 {
	      name = (String) fontComboBox.getSelectedItem();
	      b=0;i=0;u=1;
	      size = (Integer) sizeComboBox.getSelectedItem();
	      int style;
	      if (boldCheck.isSelected() && italCheck.isSelected())
	      {
	        style = Font.BOLD | Font.ITALIC; 
	        b=1;
	      	i=1;
	      }
	      else if (boldCheck.isSelected())
	      {
	        style = Font.BOLD;
	      	b=1;
	      }
	      else if (italCheck.isSelected())
	      {
	        style = Font.ITALIC;
	        i=1;
	      }
	      else
	        style = Font.PLAIN;
	      
	      f = new Font(name, style, size.intValue());
	      sampleText.setFont(f);
	      
	    }
	 }
	 
	 
public static void main(String[] args)
{
	
	SimpleTextEditor window = new SimpleTextEditor();
	window.frame.setLocationRelativeTo(null);
	window.ftable.setLocationRelativeTo(null);
	window.frame.setVisible(true);
}
}





