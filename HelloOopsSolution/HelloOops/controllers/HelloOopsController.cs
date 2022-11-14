using Microsoft.AspNetCore.Mvc;

namespace HelloOops.API
{
    [ApiController]
    [Route("api")]
    public class HelloOopsController : ControllerBase
    {

        [HttpGet("response")]
        public ActionResult GetResponse()
        {
            return new JsonResult(new { data = "ok" });
        }

        [HttpGet("data")]
        public ActionResult GetData()
        {
            return new JsonResult(new { data = "1234567", timeSpent = "42" });
        }
    }
}