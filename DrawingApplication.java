
package drawingapplication;

import java.awt.Color;
import javax.swing.JFrame; // provides basic window features
import javax.swing.JLabel; // displays text and images
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// DrawingApplication refers to the application window and handles all interactions
// between the buttons and checkboxes located on the window and adds a DrawPanel
public class DrawingApplication extends JFrame {

    private final JButton clearButton; // clear all shapes that have been drawn
    private final JButton undoButton; // clears the last shape drawn
    // shows a JColorChooser dialog to allow the user to choose the first color in the gradient
    private final JButton firstColorButton; 
    // show a JColorChooser dialog to allow the user to choose the second color in the gradient
    private final JButton secondColorButton; 
    
    private final JCheckBox filledCheckBox; // Determines if the shape is filled
    private final JCheckBox gradientCheckBox; // Determines if a gradient will be used
    private final JCheckBox dashedCheckBox; // Determines if the shapes will be dashed
    
    private final JComboBox shapeComboBox; // combo box for choosing which shape to draw
    private final String[] shapes = {"Line","Oval","Rectangle"}; // sring array for combobox
    
    private final DrawPanel drawPanel;
    
    private final JLabel comboBoxLabel;
    private final JLabel lineWidthLabel;
    private final JLabel dashLengthLabel;
    
    private final JTextField lineWidthField; // Sets the width of the stroke used to draw the shape
    private final JTextField dashLengthField;
    
    private Color color1; // first color of gradient
    private Color color2; // second color of gradient
    private Paint newPaint;
    private int width;
    private int length;
    private Stroke newStroke;
    private final float[] dashes = new float[1];
        
    
    
    // Constructor
    public DrawingApplication () {
        
        super("My Drawing Application"); // Title of the window
        setLayout( new FlowLayout() );
        drawPanel = new DrawPanel();
        
        // Initialize labels
        comboBoxLabel = new JLabel("Shape:");
        lineWidthLabel = new JLabel("Line Width:");
        dashLengthLabel = new JLabel("Dash Length:");
        
        // Initialize buttons with their text
        clearButton = new JButton("Clear");
        undoButton = new JButton("Undo");
        firstColorButton = new JButton("1st Color..");
        secondColorButton = new JButton("2nd Color..");
        
        // Initialize check boxes
        filledCheckBox = new JCheckBox("Filled");
        gradientCheckBox = new JCheckBox("Use Gradient");
        dashedCheckBox = new JCheckBox("Dashed");
        
        // Initialize text fields
        lineWidthField = new JTextField("0");
        dashLengthField = new JTextField("0");
        
        // Initialize combo box
        shapeComboBox = new JComboBox(shapes);
        shapeComboBox.setMaximumRowCount(3); // How many rows to dispaly when clicked
        
        // Add all components to the frame
        add(undoButton);
        add(clearButton);
        add(comboBoxLabel);
        add(shapeComboBox);
        add(filledCheckBox);
        add(gradientCheckBox);
        add(firstColorButton);
        add(secondColorButton);
        add(lineWidthLabel);
        add(lineWidthField);
        add(dashLengthLabel);
        add(dashLengthField);
        add(dashedCheckBox);
        add(drawPanel);
        add(drawPanel.getStatusBar());
        
        // Create and register listener for button events
        ColorButtonHandler colorButtonHandler = new ColorButtonHandler();
        UndoButtonHandler undoButtonHandler = new UndoButtonHandler();
        ClearButtonHandler clearButtonHandler = new ClearButtonHandler();
        firstColorButton.addActionListener(colorButtonHandler);
        secondColorButton.addActionListener(colorButtonHandler);
        undoButton.addActionListener(undoButtonHandler);
        clearButton.addActionListener(clearButtonHandler);
        
        // Create and register listener for check boxes
        CheckBoxHandler checkBoxHandler = new CheckBoxHandler();
        filledCheckBox.addItemListener(checkBoxHandler);
        dashedCheckBox.addItemListener(checkBoxHandler);
        gradientCheckBox.addItemListener(checkBoxHandler);
        
        // Create and register listener for combo box
        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();
        shapeComboBox.addItemListener(comboBoxHandler);
        
        // Create and register listener for text fields
        TextFieldHandler textFieldHandler = new TextFieldHandler();
        lineWidthField.addActionListener(textFieldHandler);
        dashLengthField.addActionListener(textFieldHandler);
        
    } /// end of constructor
    
    
    private class ColorButtonHandler implements ActionListener {
        
        @Override
        public void actionPerformed( ActionEvent event ) {
            
            // If the user has clicked the first color button..
            if( event.getSource() == firstColorButton ) {
                // showDialog takes the parameters of the parent window, window title, and initial color choice
                color1 = JColorChooser.showDialog(DrawingApplication.this, 
                        "Choose a color", null);
                drawPanel.setCurrentColor(color1); // set the current color to the one the user has chosen
            }
            
            // If the user has clicked th second color button..
            if( event.getSource() == secondColorButton ) {
                // showDialog takes the parameters of the parent window, window title, and initial color choice
                color2 = JColorChooser.showDialog(DrawingApplication.this, 
                        "Choose a color", null);
                drawPanel.setCurrentColor(color2); // set the current color to the one the user has chosen
            }
            
            // If both colors have been selected and the gradient check box has been selected..
            if( gradientCheckBox.isSelected() && color1 != null && color2 != null) {
                // Create a new paint with the gradient colors and set to current color
                newPaint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                drawPanel.setCurrentColor(newPaint);
            }
            
            
        } // end of actionPerformed
    } // end of ColorButtonHandler inner class
    
    
    private class UndoButtonHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
            // Calls the function to undo the last shape declared in the DrawPanel class
            drawPanel.clearLastShape();
            // Indicate the the panel should call the paintComponent method
            repaint();
        } // end of actionPerformed
    } // end of UndoButtonHandler
    
    
    
    
    private class ClearButtonHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
            // Calls the function to clear all shapes declared in the DrawPanel class
            drawPanel.clearDrawing();
            // Indicate the the panel should recall the paintComponent method
            repaint();
        } // end of actionPerformed
    } // end ClearButtonHandler
    
    
    private class CheckBoxHandler implements ItemListener {
        
        @Override
        public void itemStateChanged(ItemEvent event) {
            
            if( event.getItemSelectable() == filledCheckBox ) {
                if( filledCheckBox.isSelected() )
                    drawPanel.setFilledShape(true);
                else
                    drawPanel.setFilledShape(false);
            }
            
            if( event.getItemSelectable() == dashedCheckBox ) {
                // If dashed checkbox is selected, make stroke dashed
                if( dashedCheckBox.isSelected() && length != 0 ) {
                    newStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND, 10, dashes, 0);
                    drawPanel.setCurrentStroke(newStroke);
                }
                else {
                    newStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, 
                            BasicStroke.JOIN_ROUND);
                    drawPanel.setCurrentStroke(newStroke);
                }
            }
            
            if( event.getItemSelectable() == gradientCheckBox ) {
                
                if( gradientCheckBox.isSelected() ) {
                    if(color1 != null && color2 != null) {
                        newPaint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                        drawPanel.setCurrentColor(newPaint);
                    }
                }
                else if( color1 != null )
                    drawPanel.setCurrentColor(color1);
                else if( color2 != null )
                    drawPanel.setCurrentColor(color2);
                else 
                    drawPanel.setCurrentColor(Color.BLACK);
            }
        } // end of itemStateChanged method
    } // end FillCheckBoxHandler
    
    
    
    
    private class ComboBoxHandler implements ItemListener {
         @Override
         public void itemStateChanged(ItemEvent event) {
             
             if( event.getStateChange() == ItemEvent.SELECTED ) {
                int index = shapeComboBox.getSelectedIndex();
                drawPanel.setShapeType(index);
             }
         }// end of itemStateChanged method
    } // end of ComboBoxHandler
    
    
    private class TextFieldHandler implements ActionListener {
        
        private String input = "";
        
        @Override
        public void actionPerformed(ActionEvent event) {
            
            // If user pressed enter in the lineWidthField text field
            if( event.getSource() == lineWidthField ) {
                input = event.getActionCommand(); // get what the user has entered
                width = Integer.parseInt(input); // convert the string to an integer
            }
            
            // If user pressed enter in the dashLengthField text field
            if( event.getSource() == dashLengthField ) {
                input = event.getActionCommand(); // get what the user has entered
                length = Integer.parseInt(input); // convert the string to an integer
                dashes[0] = length; // set the only element in container dashes to the length entered 
            }
            
            // If dashed check box is not selected or is set to zero, make a normal stroke
            if( !dashedCheckBox.isSelected() || length == 0 ) {
                newStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND);
                drawPanel.setCurrentStroke(newStroke);
            }
            // Else, if the dashed check box is selected, make a dashed stroke
            else {
                newStroke = new BasicStroke(width, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND, 10, dashes, 0);
                drawPanel.setCurrentStroke(newStroke);
            }
        }
    } // end WidthTextFieldHandler
    
    
    public static void main(String[] args) {
        
        // Create a new drawing application called myDrawing
        DrawingApplication myDrawing = new DrawingApplication();
        myDrawing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        myDrawing.setSize(630,600); // set the size of the window
        myDrawing.setVisible(true); // make the window visible
    } // end of main
    
} // end of DrawingApplication class
