import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import util.SomeTestUtil

object SomeLocalTest : Spek({
	describe("some local test") {
		it("should work") {
			SomeUtil.doSomething() // has access to main
			SomeTestUtil.doSomething() // has access to testFixtures
		}
	}
})
