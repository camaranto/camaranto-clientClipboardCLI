/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputOutput;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public abstract class fileReader {
    private final File file;
    public fileReader(File file){
        this.file = file;
    }
    
    public void readFile(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){
                lineProcesser(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(fileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public abstract void lineProcesser(String line);
}
