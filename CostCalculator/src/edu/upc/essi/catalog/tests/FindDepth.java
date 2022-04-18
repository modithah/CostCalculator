package edu.upc.essi.catalog.tests;

import edu.upc.essi.catalog.constants.Const;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class FindDepth {
    int depth =0;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public static void main(String[] args) {
        String path= Const.HG_LOCATION_BOOK;
        logger.info(String.valueOf(maxDepth(Paths.get("C:\\hyper\\test\\1"))));
        int maxw=0;
        try {
            Object[] list = Files.list(Paths.get(path)).filter(p -> Files.isDirectory(p)).toArray();
            ArrayList<Integer> w = new ArrayList<>();
            ArrayList<Integer> d = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
//                w.addAll(maxWidth((Path)list[i]));
                d.add(maxDepth((Path)list[i]));
//                    logger.info(depth)
            }
//            logger.info(w.size()+","+d.size());
//            logger.info("width "+Collections.max(w)+"," + w.stream().mapToInt(a->a).average());
//            logger.info("depth "+Collections.max(d)+"," + d.stream().mapToInt(a->a).average());

            for (Integer integer : d) {
                logger.info(String.valueOf(integer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int maxDepth(Path path){
        int val=-1;
        try {
            Object[] list =  Files.list(path)
                    .filter(p -> Files.isDirectory(p)).toArray();
            if(list.length==0)
                val= 0;
            else{
                ArrayList<Integer> arr= new ArrayList<>();

                for (int i = 0; i < list.length; i++) {
                    int depth=maxDepth((Path)list[i]);
//                    logger.info(depth);
                    if(depth>val){
                        val=depth+1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return val;
    }


    public static ArrayList<Integer> maxWidth(Path path){
        int val=-1;
        ArrayList<Integer> ws= new ArrayList<>();
        try {
            Object[] list =  Files.list(path)
                    .filter(p -> Files.isDirectory(p)).toArray();
            val= list.length;
            ws.add(val);
                for (int i = 0; i < list.length; i++) {

//                    int depth=maxWidth((Path)list[i]);
                    ws.addAll(maxWidth((Path)list[i]));
//                    logger.info(depth);
//                    if(depth>val){
//                        val=depth;
//                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ws;
    }
}
