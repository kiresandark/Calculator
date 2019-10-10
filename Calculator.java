/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author psidelnikov
 */

package calculator;

import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calculator {
    /**
     *  The main class of the program.
     */
    private static JTextArea textArea;
    private static ArrayList<JButton> button;
    private static ArrayList<String> arithmeticSignList;

    public static void main(String[] args) throws IOException{
        /**
         *Main class method
         *Contains links to all necessary objects and their methods.
         */
        arithmeticSignList=new ArrayList<>();
            arithmeticSignList.add("*");
            arithmeticSignList.add("/");
            arithmeticSignList.add("-");
            arithmeticSignList.add("+");
            arithmeticSignList.add("(");
            arithmeticSignList.add(")");
        Calculator GUI = new Calculator();
        GUI.gui();
    }

    private void gui() {
        /**
         * Method for creating GUI
         * Declare the array and fill it with the necessary buttons
         * Announces other objects needed for the GUI
         * Change the size of the buttons
         * Assign each button to a general listener.
         * The listener accepts their value, after processing the resulting string
         * Assign button to the panel
         * Set frame parameters
         */

        button=new ArrayList<>();
        button.add(new JButton(""));
        button.add(new JButton("CE"));
        button.add(new JButton("C"));
        button.add(new JButton("▼"));
        button.add(new JButton("/"));
        button.add(new JButton(""));
        button.add(new JButton("7"));
        button.add(new JButton("8"));
        button.add(new JButton("9"));
        button.add(new JButton("*"));
        button.add(new JButton("π"));
        button.add(new JButton("4"));
        button.add(new JButton("5"));
        button.add(new JButton("6"));
        button.add(new JButton("-"));
        button.add(new JButton("-+"));
        button.add(new JButton("1"));
        button.add(new JButton("2"));
        button.add(new JButton("3"));
        button.add(new JButton("+"));
        button.add(new JButton("("));
        button.add(new JButton(")"));
        button.add(new JButton("0"));
        button.add(new JButton("."));
        button.add(new JButton("="));

        textArea=new JTextArea("0",4,20);
        JScrollPane scroller=new JScrollPane(textArea);
        JPanel panel=new JPanel(new GridLayout(5,4));
        ButtonListener listener = new ButtonListener();
        JFrame frame = new JFrame("Calculator-PabloEdition");
        Font bigFont = new Font("TimesRoman", Font.PLAIN, 30);



        for (JButton but : button) {
            /**
             * Fill the array elements
             * Set the size of the buttons
             * Set the text of the object font
             * Add buttons to the panel
             */
            but.addActionListener(listener);
            but.setFont(bigFont);
            panel.add(but);
        }

        textArea.setEditable(false);                      //Запрещаем изменять текст
        textArea.setLineWrap(true);                       //Разрешаем перенос строк
        textArea.setFont(new Font("TimesRoman",Font.BOLD,30)); //Задаем текстовый шрифт

           //Разрешаем прокручивать по вертикали
        scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //Запрещаем прокручивать по горизонтали
        scroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //Завершаем работу программы при выходе из фрейма
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Добавляем panelONE во фрем по центру
        frame.getContentPane().add(BorderLayout.CENTER,panel);
         //Добавляем поле текста во фрем на севере
        frame.getContentPane().add(BorderLayout.NORTH,scroller);
        frame.setSize(408,614);
        frame.setVisible(true);
    }

    private class ButtonListener implements ActionListener {
    /*
     *Класс события для кнопок
     *Передаем значение текста кнопок в text
    */
        
        boolean isEmpty = true;
        boolean isEmptyLine=true;
        boolean isOper = false;
        boolean isDot = false;
        boolean isNegated = false;
        boolean isCalculated = false;

        String eventString = "";
        
        int bracketsCount = 0;
        int lastNumberLength = 0;
        int lastBracketLength = 0;
        int length = 0;

        private String setEmpty() {
            lastNumberLength = 0;
            bracketsCount = 0;               
            lastBracketLength = 0;
            isEmptyLine = true;
            isEmpty = true;
            isOper = false;
            isDot = false;
            isNegated = false;
            isCalculated = false;   
            return "0";  
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {
            /**
             * This method is a GUI using interface.
             *Thanks to this method it is possible to edit the request.
             *Also, this method limits the input for
             *correctness of the request and its subsequent execution.
             */
            
            eventString = ((JButton)event.getSource()).getText();
            String text = textArea.getText();
            length = text.length();
            String lastOper = text.substring(length-1);

            if (isCalculated) {
                text = setEmpty();     
            }
            switch (eventString) {
                case "C" :
                    text = setEmpty();
                    break;                
                case"CE":
                    if(isEmpty) { 
                        break;
                    }
                    if(bracketsCount==0 && lastOper.contains(")")) {
                        text = text.substring(0,length-lastBracketLength);
                        bracketsCount = 0;
                        lastBracketLength = 0;
                    } else {
                        text = text.substring(0, length-lastNumberLength);
                    }
                    
                    if (text.length() == 0) {
                         text = setEmpty();
                         break;
                    }                     
                    
                    lastOper = text.substring(text.length());
                    
                    if(lastOper.contains("(") || lastOper.contains(")")) {
                        isOper = false;
                    } else {
                        isOper = true;
                    }

                    isDot = false;
                    isNegated = false;
                    lastNumberLength = 0;

                    break;
                case "▼":
                    if (length == 0) {
                        return;
                    }  
                    if (lastNumberLength == 0) {
                        return;
                    }
                    
                    if(lastOper.contains(".")) {
                        isDot = false;
                        if(text.equals("0.")) {
                            text = setEmpty();
                            length = text.length();
                        }
                    }

                    text = text.substring(0, length-1);
                    length = text.length();
                    lastOper = text.substring(length-1);
                    lastNumberLength--;
                                        
                    if(lastNumberLength==0) {
                        isOper = true;
                    }
                    
                    if(lastOper.contains("(")) {
                        isOper=false;
                    }

                    if(lastNumberLength>0 && lastOper.contains("-")) {
                        text = text.substring(0, length-1);
                        length = text.length();
                        isNegated = false;
                        lastNumberLength--;
                        isOper = true;
                    }

                    if (text.length() == 0) {
                         text = setEmpty();                        
                    }
                    break;
                case "/" :
                case "*" :
                case "-" :
                case "+" :
                    if(lastOper.contains("(")) {
                        text += "0";
                        lastNumberLength++;
                        length++;
                    }

                    if (isOper) {
                        text = text.substring(0, length-1);
                    } else {
                        
                        if(bracketsCount>0){
                            lastBracketLength += lastNumberLength+1;
                        } else {
                            lastBracketLength = 0;
                        }
                    }

                    text += eventString;

                    lastNumberLength = 0;

                    isDot = false;
                    isEmpty = false;
                    isNegated = false;
                    isOper = true;
                    break;
                case "-+":
                    if(lastNumberLength==0) {
                        break;
                    }
                    
                    if (isEmpty) {
                        lastNumberLength++; //при isEmpty тект инициализируется 0 
                    }
                    
                    if (lastNumberLength > 0) {
                        int substrLenght = length-lastNumberLength;
                        if (isNegated) {
                            text = text.substring(0, substrLenght) + text.substring(substrLenght+1);
                            lastNumberLength--;  
                        } else {
                            text = text.substring(0, substrLenght) + "-" + text.substring(substrLenght);
                            lastNumberLength++;
                        }    
                    }
                        
                    isEmpty = false;
                    isNegated = !isNegated; 
                    break;
                case "(" :
                    if(text.substring(length-1).contains(")")) {
                        return;
                    }
                    if (lastNumberLength > 0) {
                        return;
                    }
                    
                    if (isEmpty) {
                        text = "";   
                    }
                    
                    
                    bracketsCount++;
                    lastBracketLength++;
                    isEmpty = false; 
                    isOper = false;
                    isDot = false;
                    isNegated = false; 
                    lastNumberLength = 0;
                    text += eventString;                         
                    break;
                case ")" :
                    if (bracketsCount == 0) {
                        return;
                    }
                    
                    if ((lastNumberLength==0 && text.substring(length-1).contains("(")) || isOper) {
                        text += "0";
                        lastNumberLength++;
                        isEmpty = false;
                        isOper = false;
                        isNegated = false;
                    }
                    
                    bracketsCount--;
                    isEmpty = false; 
                    isOper = false;
                    isDot = false;
                    isNegated = false; 
                    lastBracketLength += lastNumberLength+1;
                    lastNumberLength = 0;
                    text += eventString;

                    break;
                case "=" :
                    if (lastNumberLength==length || isOper) {
                        return;
                    }
                    
                    for(int i = 0;i <bracketsCount;i++) {
                        text += ")";
                    }
                    
                    text = functionPerformed(text);
                    isCalculated = true;
                    break;
                case "." :
                    if(lastOper.contains(")")) {
                        return;
                    }

                    if (isDot) {
                        return;
                    }

                    if(isEmpty) {
                        lastNumberLength++;
                    }

                    if (isOper || lastOper.contains("(")) {
                        text += "0"; 
                        lastNumberLength++;
                    }
                    
                    isEmpty = false; 
                    isOper = false;
                    isDot = true;
                    lastNumberLength++;
                    text += eventString;
                    break;                  
                case "π":
                    if(lastOper.contains(")")) {
                        return;
                    }

                    if (isEmpty) {
                        text = "";
                    }

                    if (lastNumberLength > 0) {
                        text = text.substring(0, length-lastNumberLength);     
                        lastNumberLength = 0;
                    }                                       

                    isEmpty = false; 
                    isOper = false;
                    isDot = true;
                    isNegated = false; 
                    lastNumberLength = Double.toString(Math.PI).length();
                    text += Double.toString(Math.PI);                    
                    break;
                case "":
                    break;
                case "0":
                    if(lastNumberLength<=1 
                            && lastOper.contains("0")) {
                        return;
                    }                    
                default :
                    if(lastOper.contains(")")) {
                        return;
                    }
                    if(lastOper.contains("0") && lastNumberLength==1) {
                        text = text.substring(0, length-1);
                    }

                    if (isEmpty) {
                        text = "";
                        isEmpty = false;
                    }
                    
                    isOper = false;
                    lastNumberLength++;
                    text += eventString;
                    break;
            }
            textArea.setText(text);

            System.err.println(
                    "\n--------------------------------------------\n     --ДАННЫЕ ПО КАЛЬКУЛЯТОРУ--    "
                    +"\neventString  "+eventString
                    +"\nlastOper  "+lastOper
                    +"\nbracketsCount  " + bracketsCount
                    +"\nlastNumberLength  "+lastNumberLength
                    +"\nlastBracketLength  "+lastBracketLength
                    +"\nisEmpty  "+isEmpty
                    +"\nisOper  "+isOper
                    +"\nisDot  "+isDot
                    +"\nisCalculated  "+isCalculated
                    +"\nisNegated  "+isNegated
                    +"\nlength  "+text.length()
                    +"\n--------------------------------------------"
            );
        }

        public String functionPerformed(String inputText) {
            /*
             * Данный метод возращает результат решения данной ему строки.
             * Разбивает строку на отдельные элементы.
             * После проверяет на наличие арифметических знаков.
             * При нахождении арифметических символов производит арифметическое
             * действие по степени приоритета символа.
             * Возращает результат.
            */
            ArrayList<String> lineList = new ArrayList<>();
            int index = 0;

            while (index < inputText.length()) {
                int numberLength = inputText.length();
                String arithmeticSign = null;
                
                for (String sign : arithmeticSignList) {
                    
                    int nearestSignPosition = inputText.indexOf(sign, index);

                    if ((nearestSignPosition != -1 ) && (nearestSignPosition < numberLength)) {
                        
                        arithmeticSign = sign;
                        numberLength = nearestSignPosition;
                        
                    }
                }
                
                String number = inputText.substring(index, numberLength);
                if(!number.isEmpty()) {
                    if (!lineList.isEmpty() && lineList.get(lineList.size()-1).equals("-") && (lineList.size()==1 || arithmeticSignList.contains(lineList.get(lineList.size()-2)))) {
                        number = "-" + number;    
                        lineList.remove(lineList.size()-1);
                    }
                    lineList.add(number);                           
                }
                if((arithmeticSign != null)) {
                    lineList.add(arithmeticSign);                       
                }                 
                
                /*
                System.err.println(arithmeticSign);
                if(!number.isEmpty()) {
                    if(lineList.size()>2
                            &&lineList.get(lineList.size()-1).contains("-")){
                        
                        for(int i = 0; i<arithmeticSignList.length-2;i++){
                            if(lineList.get(lineList.size()-2).contains(arithmeticSignList[i])) {
                                lineList.set(lineList.size()-1, lineList.get(lineList.size()-1)+number);
                            }
                        }
                    } 

                    if(lineList.contains("-")&&lineList.size()==1) {
                        lineList.set(0,lineList.get(0)+number);
                    } else {
                        lineList.add(number);
                    }
                }
                */


                index = numberLength + 1;
                System.err.println(lineList);
            }
            
            try{
                
                while(lineList.contains("(")) {
                    int indexSkobStart=lineList.lastIndexOf("(");
                    int indexSkobEnd=0;
                
                    for(int i = indexSkobStart;i < lineList.size();i++) {
                        if(lineList.get(i).contains(")")){
                            indexSkobEnd=i;
                            break;
                        }
                    }
                    
                    ArrayList<String> lineResult = new ArrayList<>();
                    lineResult.addAll(lineList.subList(indexSkobStart+1, indexSkobEnd));

                    for(int i = indexSkobStart+1;i <= indexSkobEnd;i++) {
                        lineList.remove(indexSkobStart);
                    }
                    
                    lineList.set(indexSkobStart, count(lineResult));
                    
                }
            }
            catch(IllegalArgumentException e) {
                return "IllegalArgument";
            }

            catch(IndexOutOfBoundsException e) {
                return "IndexOutOfBounds";
            }

            String result = count(lineList);
           
            return result;
        }

        public String count(ArrayList<String> inputLine) {
            try{
                ArrayList<String> countList = inputLine;
                countList.addAll(countList);
                for ( int i = 0; i < arithmeticSignList.size()-2; i++){
                    while(countList.contains(arithmeticSignList.get(i))){

                        
                        int key = countList.indexOf(arithmeticSignList.get(i));
                        double finalResult = 0;
                        double number1 = Double.valueOf(countList.get(key-1));
                        double number2 = Double.valueOf(countList.get(key+1));

                        switch (countList.get(key)) {

                            case "/":
                                finalResult = number1 / number2;
                                break;
                            case "*":
                                finalResult = number1 * number2;
                                break;
                             case "-":
                                finalResult = number1 - number2;
                                break;
                            case "+":
                                finalResult = number1 + number2;
                                break;

                            default:
                                break;
                        }
                        
                        DecimalFormat df = new DecimalFormat("0");
                        df.setMaximumFractionDigits(340);
                        
                        countList.set(key,  df.format(finalResult));
                        countList.remove(key + 1);
                        countList.remove(key - 1);
                    }
                }
                
                 return countList.get(0);
            }
            catch(IllegalArgumentException e) {
                return "IllegalArgument_count";
            }
            catch(IndexOutOfBoundsException e) {
                return "IndexOutOfBounds";
            }
       
        }
    }
}

            // if ((nearest == resultText.length()) ) {// ошибка
                 //   line.add( resultText.substring(index, nearest));
                //} else {
                   // lineList.add( resultText.substring(index, nearest));
                //}
