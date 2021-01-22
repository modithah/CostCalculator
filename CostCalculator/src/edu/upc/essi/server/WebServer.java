package edu.upc.essi.server;

import edu.upc.essi.catalog.core.constructs.Atom;
import edu.upc.essi.catalog.core.constructs.Workload;
import org.hypergraphdb.util.Pair;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.*;

import static spark.Spark.*;

public class WebServer {


    private static String workload = null;
    private static File uploadDir = new File("C:\\hyper\\upload");

    public static void main(String[] args) {
        WebServer server = new WebServer();
        uploadDir.mkdir();
        server.startServer();
    }

    public void startServer(){
        get("/hello", (req, res) -> setWorkload("dsda"));
        get("/run",(req, res) -> ShotgunHillClimbing.run(0.25,0.25,0.25,0.25,null));

        post("/uploadWorkload", (req, res) -> {

            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";

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

}
