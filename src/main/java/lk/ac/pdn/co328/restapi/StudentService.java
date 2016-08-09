
package lk.ac.pdn.co328.restapi;
import lk.ac.pdn.co328.studentSystem.Student;
import lk.ac.pdn.co328.studentSystem.dbimplementation.DerbyStudentRegister;
import org.jboss.resteasy.util.HttpResponseCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("rest")
public class StudentService
{
    private static DerbyStudentRegister  register;

    public StudentService(){
        try {
            this.register = new DerbyStudentRegister();
        }catch (Exception e){
            System.out.print(e);
        }
    }

    @GET
    @Path("student/{id}")
    // Uncommenting this will let the reciver know that you are sending a json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON)
    public Response viewStudent(@PathParam("id") int id) throws Exception {
        Student st = register.findStudent(id);
        if(st == null){
            return Response.status(HttpResponseCodes.SC_NOT_FOUND).build();
        }
        return Response.status(HttpResponseCodes.SC_OK).entity(st).build();
    }

    @POST
    @Path("student/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON)
    public Response modifyStudent(@PathParam("id") int id, Student input) throws Exception {
        System.out.println(input.getFirstName());
        if(input == null) {
            try {
                register.addStudent(input);
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
            }
        }
        else{
            register.removeStudent(id);
            try {
                register.addStudent(input);
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(HttpResponseCodes.SC_FOUND).entity("Error.Student is modified.").build();
            }
        }
        return Response.status(HttpResponseCodes.SC_OK).build();
    }

    @DELETE
    @Path("student/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") int id) throws Exception {
        if ((register.findStudent(id) != (null))) {
            try {
                register.removeStudent(id);
               return Response.status(HttpResponseCodes.SC_OK).build();
            } catch (Exception e) {
                return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
            }
        }else {
            return Response.status(HttpResponseCodes.SC_NOT_FOUND).build();
        }
    }

    @PUT
    @Path("student/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON)
    public Response addStudent(Student input) {

        if (input != (null)) {
            try {
                register.addStudent(input);
                return Response.status(HttpResponseCodes.SC_OK).build();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(HttpResponseCodes.SC_BAD_REQUEST).build();
            }
        }else{
            return Response.status(HttpResponseCodes.SC_BAD_REQUEST).build();
        }
    }
}