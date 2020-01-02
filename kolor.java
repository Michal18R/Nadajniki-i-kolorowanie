import net.sourceforge.gxl.*;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.LinkedList;

public class kolor {


    public static void main(String[] args) {


        File GXLFile = new File("C:\\Users\\Hedi\\IdeaProjects\\nadajniki\\nadajniki.gxl");
        GXLGraph graph;


        Map<String,GXLNode> nodes = new HashMap<>();
        List<GXLEdge> edges = new LinkedList();
        Map<String,List<String>> neighbours = new HashMap<>();

        try {
            GXLDocument doc = new GXLDocument(GXLFile);
            graph = (GXLGraph) doc.getElement("graph1");
            for (int i = 0; i < graph.getGraphElementCount(); i++) {

                GXLElement el = graph.getGraphElementAt(i);

                if (el instanceof GXLNode) {

                    nodes.put(((GXLNode) el).getID(), (GXLNode) el);
                    neighbours.put(((GXLNode) el).getID(), new LinkedList <String>() );
                } else if (el instanceof GXLEdge) {

                    edges.add((GXLEdge) el);
                }


            }


        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        for(GXLEdge el: edges) {
          List<String> sourceNeighbours = neighbours.get(el.getSource().getID());
            sourceNeighbours.add(el.getTarget().getID());

            List<String> targetNeighbours = neighbours.get(el.getTarget().getID());
            targetNeighbours.add(el.getSource().getID());
      }

        List<Integer> availableColors = new LinkedList<>();
        for(int i=0;i<nodes.size();i++) {
            availableColors.add(i);
        }

        for(Map.Entry<String, List<String>> entry: neighbours.entrySet()){
            String nodeId = entry.getKey();
            List<String> nodeNeighbours = entry.getValue();

            GXLNode node = nodes.get(nodeId);

            List<Integer> neighboursColors = new LinkedList<>();
            for(String neighbourID: nodeNeighbours){
                GXLNode neighbourNode = nodes.get(neighbourID);
                if(neighbourNode.getAttr("kolor")!=null) {
                    neighboursColors.add(((GXLInt) neighbourNode.getAttr("kolor").getValue()).getIntValue());
                }
            }

            for(Integer currentColor: availableColors) {
                if(!neighboursColors.contains(currentColor)) {
                    node.setAttr("kolor", new GXLInt(currentColor));
                    System.out.println(String.format("%s kolor: %s", node.getID(), currentColor));
                    break;
                }
            }
        }
    }
}


