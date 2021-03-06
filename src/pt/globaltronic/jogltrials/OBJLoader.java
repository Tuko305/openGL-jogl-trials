package pt.globaltronic.jogltrials;

import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.jogltrials.models.RawModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static RawModel loadObjectModel(String fileName,Loader loader){
        FileReader fr = null;
        try{
            fr = new FileReader((new File("resources/" + fileName +".obj")));
        }catch(Exception ex){
            System.err.println("Couldnt load obj file!");
            ex.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vec3f> vertices = new ArrayList<Vec3f>();
        List<Vec2f> textures = new ArrayList<Vec2f>();
        List<Vec3f> normals = new ArrayList<Vec3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;

        try{
            while(true){
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if(line.startsWith("v ")){
                    Vec3f vertex = new Vec3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                }
                if(line.startsWith("vt")){
                    Vec2f texture = new Vec2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }
                if(line.startsWith("vn")){
                    Vec3f normal = new Vec3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }
                if(line.startsWith("f ")){
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }
            while (line != null){
                if(!line.startsWith("f")){
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split (" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices, textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices, textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices, textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vec3f vertex: vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i=0; i <indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }
        System.out.println("success");
        return loader.loadToVao(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indicies, List<Vec2f> textures,
                                      List<Vec3f> normals, float[] textureArray, float[] normalsArray){
        int currentVectexPointer = Integer.parseInt(vertexData[0]) -1;
        indicies.add(currentVectexPointer);
        Vec2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1) ;
        textureArray[currentVectexPointer*2] = currentTex.x;
        textureArray[currentVectexPointer*2 +1] = 1 - currentTex.y;
        Vec3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVectexPointer*3] = currentNorm.x;
        normalsArray[currentVectexPointer*3 +1] = currentNorm.y;
        normalsArray[currentVectexPointer*3 +2] = currentNorm.z;


    }
}
