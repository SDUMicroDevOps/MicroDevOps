using System.Net.Http;
using System.Text.Json;
using FluentAssertions;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace CI_UnitTests_HelloOops
{
    [TestClass]
    public class HelloOopsApiTest
    {
        [TestMethod]
        public async Task GetResponse_HasCorrectStatusCode()
        {
            //Arrange
            var client = new HttpClient();

            //Act
            var result = await client.GetAsync("http://localhost:5216/api/response");
            result = result;

            //Assert
            result.Should().BeSuccessful();
        }

        [TestMethod]
        public async Task GetData_ReturnsCorrectData()
        {
            //Arrange
            var client = new HttpClient();

            //Act
            var result = await client.GetAsync("http://localhost:5216/api/data");

            //Assert
            result.Content.Should().NotBeNull();
            var jsonData = JsonSerializer.Deserialize<Dictionary<string, string>>(await result.Content.ReadAsStringAsync());
            jsonData["data"].Should().Be("1234567");
            double.Parse(jsonData["timeSpent"]).Should().BeLessThan(100);
        }
    }

}
