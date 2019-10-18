import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.project.Project
import groovy.json.JsonBuilder
import groovy.transform.BaseScript
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

boolean isUserInProjectRole(String username, String projectRoleName, String projectKey){
    boolean result = false
    try{
		ApplicationUser user = ComponentAccessor.getUserManager().getUserByName(username)
        ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
        ProjectRole projectRole = projectRoleManager.getProjectRole(projectRoleName)
        Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey)
        result = projectRoleManager.isUserInProjectRole(user, projectRole, project)
    }catch(Exception e){
        log.error(e.getMessage())
    }
    return result
}

@BaseScript CustomEndpointDelegate delegate
isUserInProjectRole(httpMethod: "GET") { MultivaluedMap queryParams ->
    String username = queryParams.getFirst("username")
    String projectRoleName = queryParams.getFirst("projectRoleName")
    String projectKey = queryParams.getFirst("projectKey")
	boolean result = isUserInProjectRole(username, projectRoleName, projectKey)
    return Response.ok(new JsonBuilder(result).toString()).build();
}
