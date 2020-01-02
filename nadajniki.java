/**
 * Created by Hedi on 2019-05-05.
 */


import net.sourceforge.gxl.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class nadajniki {




 //   GXLDocument gxlDocument = new GXLDocument();
    private static Random random;

    public static void main(String[] args) {



        random = new Random(System.currentTimeMillis());

        Scanner myObj = new Scanner(System.in);
        System.out.println("Podaj liczbę nadajników");
        int count = myObj.nextInt();

        System.out.println("Podaj zasięg nadajników");
        int range = myObj.nextInt();

        System.out.println("Podaj promień miasta");
        int city = myObj.nextInt();

        int transmittersCount = count;
        int transmitterRange = range;
        int cityRadius = city;

        new nadajniki().generate(transmittersCount, transmitterRange, cityRadius);

    }

    private static class Transmitter {
        public int x, y;
        public String uuid;
        public Transmitter(String uuid, int x, int y) {
            this.x = x;
            this.y = y;
            this.uuid = uuid;
        }
    }

    private class Edge {
        public Transmitter a, b;
        public Edge(Transmitter a, Transmitter b) {
            this.a = a;
            this.b = b;
        }
    }

    // poniżej fragment odpowiedzialny za rozlosowaniu nadajników
    private void generate(int transmittersCount, int transmitterRange, int cityRadius) {

        GXLDocument gxlDocument = new GXLDocument();
        GXLGraph graph = new GXLGraph("graph1");

        List< GXLNode> transmitters = new LinkedList< GXLNode>();
        List<Edge> edges = new LinkedList<Edge>();

        for(int i=0;i<transmittersCount;i++) {
            int x = random.nextInt((cityRadius-1)*2)-(cityRadius-1);
            double maxY = Math.sqrt(Math.pow(cityRadius, 2) - Math.pow(Math.abs(x), 2));
            int y = random.nextInt((int)(maxY*2.0d))-(int)maxY;


            GXLNode node1 = new GXLNode(UUID.randomUUID().toString());

            node1.setAttr("x",new GXLInt (x));
            node1.setAttr("y",new GXLInt (y));

            graph.add(node1);
            transmitters.add(node1);
        }


        //poniżej fragment tworzący krawędzie
        for(int i=0;i<transmitters.size();i++) {
                for(int j=i+1;j<transmitters.size();j++) {
                    GXLNode t1 = transmitters.get(i);
                    GXLNode t2 = transmitters.get(j);
                    int t1y = ((GXLInt) t1.getAttr("y").getValue()).getIntValue(); // pobieranie z xml czegoś co później przerabiamy na inta
                    int t2y = ((GXLInt) t2.getAttr("y").getValue()).getIntValue();
                    int t1x = ((GXLInt) t1.getAttr("x").getValue()).getIntValue();
                    int t2x = ((GXLInt) t2.getAttr("x").getValue()).getIntValue();
                    double distance = Math.sqrt((t2y - t1y) * (t2y - t1y) + (t2x - t1x) * (t2x - t1x)); // obliczanie odległości pomiedzy punktami jako przekątna trójkata

                    if(distance <= transmitterRange*2) { // dwie linijki odpowiadają za stworzenie samej krawędzi

                        GXLEdge edge1 = new GXLEdge(t1,t2);
                        graph.add(edge1);
                    }
                }
        }

        gxlDocument.getDocumentElement().add(graph);

        try {

            gxlDocument.write(new File("nadajniki.gxl"));
        }
        catch (IOException ioe) {
            System.out.println("Error while writing to file: " + ioe);
        }
   }
}

