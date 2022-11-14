using Microsoft.VisualStudio.TestTools.UnitTesting;
using HelloOops.API;
using FluentAssertions;

namespace CI_UnitTests_HelloOops
{
    [TestClass]
    public class HelloOopsApiTest
    {
        [TestMethod]
        public async Task GetResponse_HasCorrectStatusCode()
        {
            //Arrange
            var CalcObject = new Calculations(5);

            //Act
            var result = CalcObject.Plus();

            //Assert
            result.Should().Be(10);

        }
    }
}
