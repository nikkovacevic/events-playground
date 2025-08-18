package boundary;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import model.RequestDTO;
import service.RequestService;

@Path("api/requests")
public class RequestResource {

    @Inject
    RequestService requestService;

    @POST
    public Response createRequest(RequestDTO requestDTO) {
        requestService.sendMessagesToConsumer(requestDTO);
        return Response.ok().build();
    }

}
