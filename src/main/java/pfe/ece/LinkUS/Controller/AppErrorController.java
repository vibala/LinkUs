package pfe.ece.LinkUS.Controller;


import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by DamnAug on 16/10/2016.
 */
@RestController
public class AppErrorController implements ErrorController{

    private static final String PATH = "/error";

    private boolean debugMode = true;

    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public String error(HttpServletRequest request, HttpServletResponse response){

        Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), debugMode);
        return "Error handling: " +
                errorAttributesMap.get("message") +
                "   Trace : " +
                errorAttributesMap.get("trace");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}