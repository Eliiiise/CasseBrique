import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class AirInfo extends JPanel{

    Vie[] tableauVie;
    //Déclaration des variable
    public int x, y, num=1, nbpoint=0;
    public int larg, haut;
    public int bordure;
    public Color couleurAirInfo, couleurBord;
    int vies=5;
    Briques monBouton;



    public AirInfo(int xParam, int yParam, int largParam, int hautParam, Color coulParam) {
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurAirInfo = coulParam;
        tableauVie = new Vie[5];

        monBouton = new Briques(900, 45, 80, 40,  new Color (78, 132, 255), new Color (186, 227, 255));
        for (int j=0; j<5; j++)
        {
            tableauVie[j] = new Vie (80+30*j,55,20,20,new Color(255, 0, 224));
        }

    }



    public void dessiner(Graphics g){
        g.setColor(couleurAirInfo);
        g.fillRect(x, y, larg, haut);
        for (int j=0; j<vies; j++)
        {
            if (tableauVie[j]!=null){
                tableauVie[j].dessiner(g);
            }

        }
        monBouton.dessiner(g);
        Dimension d = this.getPreferredSize();
        int fontSize = 20;
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(new Color(61, 144, 255));
        g.drawString("vie :", 30, 70 );
        g.drawString("niveau : " +num, 300, 70 );
        g.drawString("points : " +nbpoint, 500, 70 );

        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(new Color(255, 255, 255));
        g.drawString("pause", 910, 70 );


    }
}
