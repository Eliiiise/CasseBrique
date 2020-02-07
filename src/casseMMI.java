import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;


public class casseMMI extends JFrame implements KeyListener, Runnable, MouseListener {

    AirDeJeu a1;
    AirInfo ai;
    private Thread processusJeu;
    private Image doubleBuffer;
    private Graphics gBuffer;
    private JButton pauseSonButton;
    boolean audio_On;
    int nbCollisionSimultane;
    int mort=0;
    int changement=0;
    int chargement=1;
    int bonusBalleEnflamme=0;
    public String[][] tabBonusActif;
    Missile[] tabMissile;
    int cptMissiles = 0;
    int bonusMissile = 0;
    int cptMisTp ;
    int cptFlamTp ;
    int nbBalles=1 ;
    int cptTempsEntreMissiles=0;
    int nbTour=0, nbTourBis=0;
    int pause=0;


    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //1. Création d'une instance (objet) de la classe casseMMI
        casseMMI monJeu = new casseMMI();
    }

    //2. Constructeur (Méthode qui construit un objet de la classe concernée ici casseMMI)
    public casseMMI(){
        super("casseMMI 2019-2020");
        this.setSize(1020,700);
        ai = new AirInfo(0,0,1020,100, new Color(186, 227, 255));
        a1 = new AirDeJeu(0,100,1020,600, new Color(220, 245, 255),ai.num);
        this.setVisible(true);
        doubleBuffer = createImage(getSize().width, getSize().height);
        gBuffer = doubleBuffer.getGraphics();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        processusJeu=new Thread(this);
        processusJeu.start();
        addKeyListener(this);
        addMouseListener(this);
        this.setLayout(null);
        pauseSonButton= new JButton("SON ON/OFF");
        pauseSonButton.setBounds(50, 650, 150, 40);
        this.add(pauseSonButton);
        pauseSonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (audio_On==true) {audio_On=false;}
                else audio_On=true;
            }
        });
        tabBonusActif= new String[20][16];
        tabMissile = new Missile[10];
    }

    public void paint(Graphics g){
        gBuffer.setColor(getBackground());
        // passe le double buffer a la couleur de fond
        gBuffer.fillRect(0, 0, getSize().width, getSize().height);
        a1.dessiner(gBuffer);
        ai.dessiner(gBuffer);
        for (int j=0; j<10; j++){
            if (tabMissile[j]!=null){
                tabMissile[j].dessiner(gBuffer);
            }
        }
        //Etat plus de vie
        if (ai.vies==0){
            a1 = new AirDeJeu(0,100,1020,600, new Color(220, 245, 255),ai.num);
            this.repaint();
            gBuffer.setColor(new Color(186, 227, 255));
            gBuffer.fillRect(0, 0, 1020, 700);
            Dimension d = this.getPreferredSize();
            int fontSize = 70;
            gBuffer.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
            gBuffer.setColor(Color.white);
            gBuffer.drawString("PERDU", 380, 300 );
            fontSize = 20;
            gBuffer.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
            gBuffer.drawString("Vous avez eu "+ai.nbpoint+" points!", 380, 330 );
            gBuffer.setColor(new Color(122, 194, 255));
            gBuffer.fillRect(410, 400, 200, 50);
            fontSize = 30;
            gBuffer.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
            gBuffer.setColor(Color.white);
            gBuffer.drawString("Rejouer", 450, 435 );
            fontSize = 20;
            gBuffer.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
            gBuffer.setColor(Color.red);
            gBuffer.drawString("appuyer sur espace", 417, 470 );
        }

        //Etat changement de niveau
        if (changement==1){
            int fontSize = 50;
            gBuffer.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
            gBuffer.setColor(Color.BLACK);
            gBuffer.drawString("NIVEAU "+ai.num, 380, 300 );
        }

        g.drawImage(doubleBuffer,0,0,this);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (pause==0){
            switch (key) {
                case KeyEvent.VK_RIGHT :
                    if(a1.maRaquette.x<a1.larg-a1.maRaquette.larg-10 && ai.vies>0) {
                    a1.maRaquette.x=a1.maRaquette.x+10;
                        //pour que la balle reste sur la raquette quand elle est pas encore partie
                        if  ( mort==1 || changement==1 || chargement==1){
                            for (int s=0; s<nbBalles; s++){
                                a1.tabBalle[s].x=a1.maRaquette.x+40;
                            }
                            this.repaint();
                        }
                    }
                    this.repaint();
                    break;

                case KeyEvent.VK_LEFT :
                    if(a1.maRaquette.x>0+10 && ai.vies>0) {
                        a1.maRaquette.x = a1.maRaquette.x - 10;
                        //pour que la balle reste sur la raquette quand elle est pas encore partie
                        if  ( mort==1 || changement==1 || chargement==1){
                            for (int s=0; s<nbBalles; s++){
                                a1.tabBalle[s].x=a1.maRaquette.x+40;
                            }
                            this.repaint();
                        }

                    }
                    this.repaint();
                    break;


                case KeyEvent.VK_SPACE :
                    //Pour recommencer une partie
                    if(ai.vies==0) {
                        ai.vies=5;
                        ai.num=1;
                        a1.num=1;
                        ai.nbpoint=0;
                        chargement=1;
                    }

                    //créer les missiles lorsqu'on appuie sur espace
                    if(bonusMissile>0 && cptTempsEntreMissiles>=10) {
                        cptMissiles=cptMissiles+1;
                        cptTempsEntreMissiles=0;
                        if (cptMissiles==10){
                            cptMissiles=1;
                        }
                        if (tabMissile[cptMissiles-1]!=null){
                            tabMissile[cptMissiles-1].x=a1.maRaquette.x+a1.maRaquette.larg/2-2;
                            tabMissile[cptMissiles-1].y=a1.maRaquette.y;
                        }
                        else{
                            tabMissile[cptMissiles-1] = new Missile(a1.maRaquette.x+a1.maRaquette.larg/2-2,550,5,50,new Color(255, 30, 0),0,-8);
                            tabMissile[cptMissiles-1].x=a1.maRaquette.x+a1.maRaquette.larg/2-2;
                            tabMissile[cptMissiles-1].y=a1.maRaquette.y;
                        }
                    }
                    this.repaint();
                    break;

            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int K1 = e.getX();
        int K2 = e.getY();
        System.out.println(K1+" "+K2);
        if (K1>900 && K1<900+80 && K2>45 &&K2<45+40 && pause==0){
            pause=1;
        }
        else if (K1>900 && K1<900+80 && K2>45 &&K2<45+40 && pause==1){
            pause=0;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        try{
            while(true)//boucle infinie
            {
                if (pause==1){//met le jeu en pause
                    Thread.sleep(15);
                }
                else{
                    for (int s=0; s<5; s++){// fais fonctionner le jeu (on peut avoir jusqu'a 6 balles)
                        if (a1.tabBalle[s].haut==20){
                             total(a1.tabBalle[s]);
                        }
                    }
                    if (bonusMissile>0){
                        cptTempsEntreMissiles=cptTempsEntreMissiles+1;
                    }
                    this.repaint();
                    Thread.sleep(15);
                }
            }//fin de la boucle
        }
        catch(Exception e){System.out.println("Le Thread plante:"+e);}
}


public void collisionAvecRaquette (Balle uneBalle){
    if(uneBalle.getHitBox().intersects(a1.maRaquette.getHitBox())){
        if(uneBalle.getB().intersects(a1.maRaquette.getHitBox())) {
            if(uneBalle.getB().intersects(a1.maRaquette.getGauche())){ //si la balle tape a gauche de la raquette
                if (uneBalle.vx<0){
                    uneBalle.vx=-6;
                    uneBalle.vy=-4;
                }
                else if (uneBalle.vx>0){
                    uneBalle.vx=-4;
                    uneBalle.vy=-4;
                }
            }
            else if(uneBalle.getB().intersects(a1.maRaquette.getDroite())){ //si la balle tape a droite de la raquette
                if (uneBalle.vx<0){
                    uneBalle.vx=4;
                    uneBalle.vy=-4;
                }
                else if (uneBalle.vx>0){
                    uneBalle.vx=6;
                    uneBalle.vy=-4;
                }
            }
            else {
                uneBalle.vy=-uneBalle.vy;
            }

            System.out.println(uneBalle.vx);
        }
        else if(uneBalle.getBd().intersects(a1.maRaquette.getHitBox())){ //coté gauche de la balle
            uneBalle.vx=-uneBalle.vx;
        }
        else if (uneBalle.getBg().intersects(a1.maRaquette.getHitBox())){  //coté droit de la balle
            uneBalle.vx=-uneBalle.vx;
        }
    }
}
public void typeCollision (int l, int m, Balle uneBalle){
    if (a1.lvl[l][m].equals("2")){
        a1.tableau[l][m].couleurBriques = new Color(164, 190, 255);
        a1.lvl[l][m]= String.valueOf(1);
    }
    else if (a1.lvl[l][m].equals("3")){
        tabBonusActif[l][m]= String.valueOf(1);
        a1.tableau[l][m]=null;
    }
    else if (a1.lvl[l][m].equals("4")){
        a1.tableau[l][m]=null;
        if (bonusBalleEnflamme==1){
            cptFlamTp=0;
        }
        bonusBalleEnflamme=1;
        uneBalle.couleurBalle = new Color(255, 196, 39);
    }
    else if (a1.lvl[l][m].equals("5")){
        tabBonusActif[l][m]= String.valueOf(1);
        a1.tableau[l][m]=null;
    }
    else if (a1.lvl[l][m].equals("6")){
        tabBonusActif[l][m]= String.valueOf(1);
        a1.tableau[l][m]=null;
    }
    else {
        a1.tableau[l][m]=null;
    }
}  //definie ce qu'il se passe par rapport a la brique qui vient d'etre percuté (bonus ou pas)
public void rebondBrique (Briques uneBrique, Balle uneBalle){
    if (uneBalle.vy>0 && uneBalle.vx>0){  //bas droite
        if(uneBalle.getD().intersects(uneBrique.getHitBox())){ //coté gauche
            uneBalle.vx=-uneBalle.vx;
        }
        else if(uneBalle.getB().intersects(uneBrique.getHitBox())) {   //haut
            uneBalle.vy=-uneBalle.vy;
        }
        else{
            uneBalle.vy=-uneBalle.vy;
            uneBalle.vx=-uneBalle.vx;
        }
    }
    else if (uneBalle.vy>0 && uneBalle.vx<0){   //bas gauche
        if(uneBalle.getG().intersects(uneBrique.getHitBox())){  //coté droit
            uneBalle.vx=-uneBalle.vx;
        }
        else if(uneBalle.getB().intersects(uneBrique.getHitBox())){   //haut
            uneBalle.vy=-uneBalle.vy;
        }
        else{
            uneBalle.vy=-uneBalle.vy;
            uneBalle.vx=-uneBalle.vx;
        }
    }
    else if (uneBalle.vy<0 && uneBalle.vx>0){   //haut droit
        if(uneBalle.getD().intersects(uneBrique.getHitBox())){ //coté gauche
            uneBalle.vx=-uneBalle.vx;
        }
        else if(uneBalle.getH().intersects(uneBrique.getHitBox())){ //bas
            uneBalle.vy=-uneBalle.vy;
        }
        else{
            uneBalle.vy=-uneBalle.vy;
            uneBalle.vx=-uneBalle.vx;
        }
    }
    else if (uneBalle.vy<0 && uneBalle.vx<0){   //haut gauche
        if(uneBalle.getG().intersects(uneBrique.getHitBox())){  //coté droit
            uneBalle.vx=-uneBalle.vx;
        }
        else if(uneBalle.getH().intersects(uneBrique.getHitBox())){ //bas
            uneBalle.vy=-uneBalle.vy;
        }
        else{
            uneBalle.vy=-uneBalle.vy;
            uneBalle.vx=-uneBalle.vx;
        }
    }
}
public void nouveauNiveau() throws InterruptedException {
    ai.num=ai.num+1;
    a1 = new AirDeJeu(0,100,1020,600, new Color(220, 245, 255),ai.num);
    this.repaint();
    System.out.println("niveau"+a1.num);
    bonusMissile=0;
    a1.maRaquette.couleurRaquette = new Color(255, 122, 156);
    cptMisTp=0;
    cptMissiles = 0;
    bonusMissile = 0;
    nbBalles=1 ;
    cptTempsEntreMissiles=0;
    nbTour=0;
    nbTourBis=0;
    pause=0;
    for (int j=0; j<16; j++)
    {
        for(int i=0; i<20; i++)
        {
            tabBonusActif[i][j]=null;
        }
    }
    for (int j=0; j<10; j++){
        tabMissile[j]=null;
    }
    this.repaint();
    changement = 1 ;
    nbBalles=1;
    for (int f=0; f<5; f++){
        a1.tabBalle[f].haut=0;
    }
    a1.tabBalle[0].haut=20;
    this.repaint();
    Thread.sleep(2*1000);
    changement = 0 ;
}
public void testCollisionBrique(Balle uneBalle) throws InterruptedException {
    int nbBriquesTotale=0;
    for (int j=0; j<16; j++)
    {
        for(int i=0; i<20; i++)
        {
            // test de collision avec les missiles
            for (int k=0; k<10; k++)
            {
                if (tabMissile[k]!=null){
                    if (a1.tableau[i][j]!=null){
                        if(tabMissile[k].getHitBox().intersects(a1.tableau[i][j].getHitBox())){
                            tabMissile[k]=null;
                            typeCollision (i, j, uneBalle);
                        }
                    }
                }
            }
            if (a1.tableau[i][j]!=null)  //pour les briques non null
            {
                nbBriquesTotale = nbBriquesTotale+1;
                if(uneBalle.getHitBox().intersects(a1.tableau[i][j].getHitBox()))//quand la balle recontre une brique
                {
                    //ajout des point lorsqu'on touche une brique
                    ai.nbpoint=ai.nbpoint+10;
                    //test pour que quand la balle entre en collision avec plusieur brique a la fois elle ne change de direction qu'une fois
                    System.out.println(uneBalle.couleurBalle);
                    if (nbCollisionSimultane==0 && uneBalle.couleurBalle== Color.CYAN){
                        rebondBrique(a1.tableau[i][j], uneBalle);
                        typeCollision (i, j, uneBalle);
                        nbCollisionSimultane=nbCollisionSimultane+1;// compte le nombre de collision qui se font en même temps
                    }
                    //collision sans rebond
                    else {
                        typeCollision (i, j, uneBalle);
                    }
                }
            }
        }
    }
    if (nbBriquesTotale==0){
        nouveauNiveau();
    }
    nbCollisionSimultane=0;
}
public void testCollisionBonus() throws InterruptedException {
    for (int j=0; j<16; j++)
    {
        for(int i=0; i<20; i++)
        {
            if (tabBonusActif[i][j]!=null){
                if(a1.maRaquette.getHitBox().intersects(a1.tabBonus[i][j].getHitBox())){
                    switch (a1.lvl[i][j]){
                        case "3" :
                            a1.tabBonus[i][j]=null;
                            tabBonusActif[i][j]=null;
                            if (ai.vies<5){
                                ai.vies=ai.vies+1;
                                this.repaint();
                                System.out.println("le nb de vie est = "+ai.vies);
                            }
                            break;
                        case "5" :
                            a1.tabBonus[i][j]=null;
                            tabBonusActif[i][j]=null;
                            System.out.println("mysileeeee");
                            a1.maRaquette.couleurRaquette = new Color(255, 0, 10);
                            if (bonusMissile==1){
                                cptMisTp=0;
                            }
                            bonusMissile=1;
                            break;
                        case "6" :
                            a1.tabBonus[i][j]=null;
                            tabBonusActif[i][j]=null;
                            System.out.println("triple balle");
                            nbBalles=nbBalles+2;
                            System.out.println(nbBalles);
                            int cptBalleEnPlus=0;
                            for (int f=0; f<5; f++){
                                if (cptBalleEnPlus<2 && a1.tabBalle[f].haut==0){
                                    a1.tabBalle[f].haut=20;
                                    a1.tabBalle[f].x=a1.maRaquette.x+40;
                                    cptBalleEnPlus=cptBalleEnPlus+1;
                                    if (cptBalleEnPlus==2){
                                        a1.tabBalle[f].vx=-a1.tabBalle[f].vx;
                                    }
                                }
                            }

                            System.out.println(a1.tabBalle[0].haut+" "+a1.tabBalle[1].haut+" "+a1.tabBalle[2].haut+" "+a1.tabBalle[3].haut+" "+a1.tabBalle[4].haut);
                            break;
                    }
                }
            }
        }
    }
}
public void perdreUneVie(Balle uneBalle) throws InterruptedException{
    nbBalles=nbBalles-1;// la derniere balle disparait
    uneBalle.haut=0;
    reinitialiserBalle(uneBalle);//pour qu'elle soit prete à etre reutiliser
    System.out.println(nbBalles);
    System.out.println(a1.tabBalle[0].haut+" "+a1.tabBalle[1].haut+" "+a1.tabBalle[2].haut+" "+a1.tabBalle[3].haut+" "+a1.tabBalle[4].haut);
    if (nbBalles==0){
        ai.vies = ai.vies-1; //on perd une vie
        nbBalles=1; //on regagne une balle
        a1.tabBalle[0].haut=20;
        uneBalle.x=a1.maRaquette.x+40;//mettre la balle sur la raquette
        bonusMissile=0; //on perd tout nos bonus
        bonusBalleEnflamme=0;
        cptMisTp=0;
        a1.maRaquette.couleurRaquette = new Color(255, 122, 156);
        //on supprime tout les missiles qui etait dans l'aire de jeu
        for (int j=0; j<10; j++){
            tabMissile[j]=null;
        }
        //on supprime tout les bonus qui été actif
        for (int j=0; j<16; j++)
        {
            for(int i=0; i<20; i++)
            {
                if (tabBonusActif[i][j]!=null){
                    tabBonusActif[i][j]=null;
                    a1.tabBonus[i][j]=null;
                }

            }
        }
        this.repaint();
        mort=1;
        Thread.sleep(2*1000);
        mort=0;
    }
}
public void resterDansAireDeJeu(Balle uneBalle) throws InterruptedException {
    if (uneBalle.x>=a1.larg-uneBalle.larg)
    {
        uneBalle.vx=-uneBalle.vx;
    }
    if (uneBalle.x<=0)
    {
        uneBalle.vx=-uneBalle.vx;
    }
    if (uneBalle.y>=a1.haut+110)
    {
        perdreUneVie(uneBalle);
    }
    if (uneBalle.y<=100)
    {
        uneBalle.vy=-uneBalle.vy;
    }
} //fait rebomdire la balle sur les bords ou perdre une vie
public void metEnMvt(Balle uneBalle){
    uneBalle.x=uneBalle.x+uneBalle.vx;
    uneBalle.y=uneBalle.y+uneBalle.vy;
}
public void total(Balle uneBalle) throws InterruptedException {
        debugBalleBloque(uneBalle);
    //Pour charger le tout premier du niveau
    if (ai.vies==5 && ai.num==1 && chargement==1){
        for (int s=1; s<5; s++){//rendre invisibles les balles inactives
            a1.tabBalle[s].haut= 0;
        }
        this.repaint();
        Thread.sleep(2*1000);
        chargement=0;
        uneBalle.vx=2;
        uneBalle.vy=-4;
    }
    //Cree la balle tant qu'on a des vie
    if (ai.vies>0){
        metEnMvt(uneBalle);

        // fait tomber les bonus de la brique bonus
        for (int j=0; j<16; j++)
        {
            for(int i=0; i<20; i++)
            {
                if (tabBonusActif[i][j]!=null){
                    if (tabBonusActif[i][j].equals("1")){
                        a1.tabBonus[i][j].y=a1.tabBonus[i][j].y+a1.tabBonus[i][j].vy;
                    }
                    this.repaint();
                }
            }
        }

        // temps de l'effet missiles
        if (bonusMissile>0){
            cptMisTp=cptMisTp+1;
            if (cptMisTp==500){
                bonusMissile = 0;
                a1.maRaquette.couleurRaquette = new Color(255, 122, 156);
                cptMisTp=0;
            }
        }
        // temps de l'effet flammes
        if (bonusBalleEnflamme>0){
            cptFlamTp=cptFlamTp+1;
            if (cptFlamTp==500){
                bonusBalleEnflamme = 0;
                uneBalle.couleurBalle = Color.CYAN;
                cptFlamTp=0;
            }
        }


        // fait bouger les missiles
        for (int j=0; j<10; j++)
        {
            if (tabMissile[j]!=null){
                tabMissile[j].y=tabMissile[j].y+tabMissile[j].vy;
                this.repaint();
            }
        }
    }

    // suprime les missiles qui sortent
    for (int j=0; j<10; j++){
        if (tabMissile[j]!=null){
            if (tabMissile[j].y<=100)
            {
                tabMissile[j]=null;
            }
        }
    }
    resterDansAireDeJeu(uneBalle); //fait rebomdire la balle sur les bords ou perdre une vie
    testCollisionBonus();
    collisionAvecRaquette(uneBalle);
    testCollisionBrique(uneBalle);
}
public void reinitialiserBalle(Balle uneBalle){
    uneBalle.x=400;
    uneBalle.y=600;
    uneBalle.vx=2;
    uneBalle.vy=-4;
    uneBalle.couleurBalle = Color.CYAN;
}
public void debugBalleBloque(Balle uneBalle){
    if (uneBalle.x<10){
        nbTour=nbTour+1;
    }
    else {
        nbTour=0;
    }
    if (nbTour>=10){
        uneBalle.x=20;
    }
    if (uneBalle.x>a1.larg-30){
        nbTourBis=nbTourBis+1;
        System.out.println(nbTourBis);
    }
    else {
        nbTourBis=0;
    }
    if (nbTourBis>=10){
        uneBalle.x=a1.larg-20;
    }

}
}

