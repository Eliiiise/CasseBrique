import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class AirDeJeu {

    Raquette maRaquette;
    Balle[] tabBalle;
    Briques[][] tableau;
    Bonus[][] tabBonus;

    //Déclaration des variables
    public int x, y, num;
    public int larg, haut;
    public int bordure;
    public Color couleurAirDeJeu, couleurBord;
    public String[][] lvl ;
    int emplacement=420;

    public AirDeJeu(int xParam, int yParam, int largParam, int hautParam, Color coulParam,int numLevel) {
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        num=numLevel;

        couleurAirDeJeu = coulParam;
        lvl = new String[20][16];
        levelLoad(num);

        maRaquette = new Raquette(emplacement, 620, 100, 15, new Color(255, 122, 156));

        tabBalle = new Balle[5];
        for (int j=0; j<5; j++){
            tabBalle[j]= new Balle(emplacement+40,600,20,20, Color.CYAN,2,-4);
        }

        tabBonus = new Bonus[20][16];
        tableau = new Briques[20][16];
         for (int j=0; j<16; j++)
        {
            for(int i=0; i<20; i++)
            {
                if (lvl[i][j].equals("1"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (164, 190, 255), new Color (186, 227, 255));
                }
                if (lvl[i][j].equals("2"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (98, 149, 255), new Color (186, 227, 255));
                }
                if (lvl[i][j].equals("3"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (255, 67, 178), new Color (186, 227, 255));
                    tabBonus[i][j] = new Bonus(10+i*50+15,100+j*15,15,15,new Color(255, 0, 224),0,2);
                }
                if (lvl[i][j].equals("4"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (255, 196, 39), new Color (186, 227, 255));

                }
                if (lvl[i][j].equals("5"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (255, 0, 10), new Color (186, 227, 255));
                    tabBonus[i][j] = new Bonus(10+i*50+15,100+j*15,15,15,new Color(255, 0, 10),0,2);
                }
                if (lvl[i][j].equals("6"))
                {
                    tableau[i][j] = new Briques(10+i*50 , 100+j*15, 50, 15,  new Color (139, 255, 203), new Color (186, 227, 255));
                    tabBonus[i][j] = new Bonus(10+i*50+15,100+j*15,15,15,new Color(139, 255, 203),0,2);
                }
            }
        }

    }

    public void dessiner(Graphics g) {
        g.setColor(couleurAirDeJeu);
        g.fillRect(x, y, larg, haut);
        maRaquette.dessiner(g);
        for (int j=0; j<5; j++){
            tabBalle[j].dessiner(g);
        }

        for (int j=0; j<16; j++)
        {
            for(int i=0; i<20; i++)
            {
               if (tableau[i][j]!=null)
               {tableau[i][j].dessiner(g);}
                if (tabBonus[i][j]!=null)
                {tabBonus[i][j].dessiner(g);}
            }
        }
    }



    public void levelLoad(int _numLevel) {
        int numLevel=_numLevel;
        try {
            FileInputStream fdtream=new FileInputStream("level"+numLevel+".txt");
            DataInputStream in = new DataInputStream(fdtream);
            BufferedReader br = new BufferedReader( new InputStreamReader(in));
            String strLine;
            int numLigne=0;
            while ((strLine = br.readLine()) !=null) {
                String[] tampon=strLine.split(";");
                for(int i=0; i<20; i++){
                    lvl[i][numLigne]=tampon[i];
                }
                System.out.println (strLine);
                numLigne++;
            }
            in.close();
        }
        catch(Exception e){
            System.err.println("Erreur :"+e.getMessage());
        }

    }

}
