package edu.upc.essi.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Workload;
import edu.upc.essi.catalog.cost.CostResult;
import edu.upc.essi.catalog.cost.DepthandHeterogeniety;
import edu.upc.essi.catalog.ops.Graphoperations;
import edu.upc.essi.catalog.optimizer.CostCalculator;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.*;

import static spark.Spark.*;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static String workload = null;
    private static String usecase =null;
    private static File uploadDir = new File("C:\\hyper\\upload");
    private double query=0.25;
    private double size=0.25;
    private double depth =0.25;
    private double hetro= 0.25;


    public static void main(String[] args) {
        WebServer server = new WebServer();
        uploadDir.mkdir();
        server.startServer();
    }



    public void startServer(){

//        after((Filter) (request, response) -> {
//            response.header("Access-Control-Allow-Origin", "*");
//            response.header("Access-Control-Allow-Methods", "GET");
//            response.header("Access-Control-Allow-Methods", "POST");
//        });

        enableCORS("*", "*", "*");

        get("/hello", (req, res) -> setWorkload("dsda"));
        get("/run/:iters",(req, res) ->{
            res.type("application/json");
            String pidString = req.params(":iters");
            int iters = 20;
            if (!(pidString == null || pidString.equals("null") || pidString.isEmpty())) {
                iters=Integer.parseInt(pidString);
            }
            JSONObject obj = ShotgunHillClimbing.run(query, size, depth, hetro, usecase, iters);
            String loc = obj.getString("location");
            HyperGraph g = new HyperGraph(loc);
            CostResult cost = CostCalculator.calculateCost(g);
            JSONArray arr = new JSONArray();
            JSONObject h = new JSONObject().put("param","Average heterogeneity").put("val",DepthandHeterogeniety.CalculateHeterogeniety(g));
            JSONObject q = new JSONObject().put("param","Query cost estimation").put("val",String.format("%.5f",cost.getQueryCosts().stream().reduce(Double::sum).get()));
            JSONObject d = new JSONObject().put("param","Average Depth").put("val",DepthandHeterogeniety.CalculateDepth(g));
            JSONObject s = new JSONObject().put("param","Size estimation").put("val", String.format("%.2f", (cost.getStorageSize()/(1000000000)))+ " GB");

            arr.put(q).put(s).put(h).put(d);
    obj.put("meta",arr);
//            obj.put("depth",DepthandHeterogeniety.CalculateHeterogeniety(g));
//            obj.put("size",);
////            obj.put("cost",);
            obj.put("design", Graphoperations.JSONDesign(g));
            obj.remove("location");
            g.close();
            return obj;
        });

        post("/uploadWorkload", (req, res) -> {
            res.type("application/json");
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            setWorkload(tempFile.toString());
            return logAndReturnSuccess("You uploaded this workload:" + tempFile.getFileName() );

        });

        post("/uploadWorkload2", (req, res) -> {
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return logAndReturnSuccess("You uploaded this workload:" + tempFile.getFileName() );

        });

        post("/uploadUsecase", (req, res) -> {
            res.type("application/json");
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            setUsecase(tempFile.toString());
            return logAndReturnSuccess("You uploaded this usecase:" + tempFile.getFileName() );

        });

        post("/weights/", (req, res) -> {
            res.type("application/json");
//            res.header("Access-Control-Allow-Origin", "*");
            JsonObject jsonObject = JsonParser.parseString(req.body()).getAsJsonObject();
            System.out.println(jsonObject);
            setQuery(jsonObject.get("Query").getAsDouble());
            setHetro(jsonObject.get("Heterogeneity").getAsDouble());
            setDepth(jsonObject.get("Depth").getAsDouble());
            setSize(jsonObject.get("Size").getAsDouble());
            return logAndReturnSuccess("weights updated");
        });



//        get(Const.START_PROFILING + ":pid", (request, response) -> {
//            response.type("application/json");
//            String pidString = request.params(":pid");
//            String error;
//
//            try {
//                Pid pid = new Pid(Long.parseLong(pidString));
//                logger.info(String.format("Request to record process %d", pid.get()));
//                RecordingId rid = startProfiling(pid);
//                if (rid == null) {
//                    error = String.format("process %d  recording failed", pid.get());
//                    return logAndReturnError(response, error);
//                } else {
//                    logger.info(String.format("process %d  recording %d started", pid.get(), rid.get()));
//                }
//                return new Gson().toJson(rid);
//            } catch (NumberFormatException e) {
//                error = "process id is not a number";
//                return logAndReturnError(response, error);
//            }
//        });
    }



    public static String getWorkload() {
        return workload;
    }
    public String setWorkload(String workload) {
        this.workload = workload;
        return workload;
    }

    public static String getUsecase(){
        return usecase;
    }

    public String setUsecase(String usecase){
        this.usecase=usecase;
        return usecase;
    }

    public double getQuery() {
        return query;
    }

    public void setQuery(double query) {
        this.query = query;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getHetro() {
        return hetro;
    }

    public void setHetro(double hetro) {
        this.hetro = hetro;
    }

    private String logAndReturnSuccess(String msg) {
        logger.info(msg);
        return "{\"OK\":200,\"message\":\"" + msg + "\"}";
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
