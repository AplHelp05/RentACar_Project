/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Datos.GlobalException;
import Datos.NoDataException;

/**
 *
 * @author david
 */
public class pr {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GlobalException, NoDataException {
        // TODO code application logic here
        Control c;
        c = new Control();
        System.out.println(c.listarAgencias());
    }
    
}
