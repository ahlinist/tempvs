package club.tempvs.library

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.UserService
import com.netflix.discovery.EurekaClient
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.access.annotation.Secured

@GrailsCompileStatic
@Secured('isAuthenticated()')
class LibraryController {

    private static final String LIBRARY_SERVICE_NAME = 'library'

    @Value('${library.security-token}')
    private final String librarySecurityToken

    static allowedMethods = [
            index: 'GET',
            admin: 'GET',
            period: 'GET',
            requestRole: 'POST',
            cancelRoleRequest: 'DELETE',
            approveRoleRequest: 'POST',
            rejectRoleRequest: 'DELETE',
    ]

    UserService userService
    SourceService sourceService
    PageRenderer groovyPageRenderer
    AjaxResponseHelper ajaxResponseHelper
    RestCaller restCaller
    EurekaClient eurekaClient

    def api(String uri) {
        String encodedToken = librarySecurityToken.encodeAsMD5() as String
        HttpMethod httpMethod = HttpMethod.valueOf(request.method)
        String serviceUrl = eurekaClient.getApplication(LIBRARY_SERVICE_NAME)?.instances?.find()?.homePageUrl
        String url = "${serviceUrl}/api/" + uri
        RestResponse restResponse = restCaller.call(url, httpMethod, encodedToken, request.JSON as JSON)
        Integer status = restResponse?.statusCode?.value()

        if (status in [200, 400]) {
            render(status: status, text: restResponse?.responseBody)
        } else {
            response.sendError(500)
        }
    }

    @Secured('permitAll')
    Map index() {
        [periods: Period.values()]
    }

    @Secured('permitAll')
    Map period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)

            [
                    sources: sources,
                    period: period,
                    itemTypes: ItemType.values(),
                    sourceTypes: SourceType.values(),
            ]
        }
    }

    @Secured('ROLE_ARCHIVARIUS')
    def admin() {

    }
}
