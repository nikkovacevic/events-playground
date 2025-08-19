package boundary;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import model.RequestDTO;
import org.jboss.logging.Logger;
import service.RequestService;

import static org.jboss.logging.Logger.getLogger;


@Path("api/requests")
public class RequestResource {

    private static final Logger log = getLogger(RequestResource.class);

    @Inject
    RequestService requestService;

    @POST
    public Response createRequest(RequestDTO requestDTO) {
        log.infof("Request received: %s", requestDTO);
        requestService.sendMessagesToConsumer(requestDTO);
        return Response.ok().build();
    }

}
