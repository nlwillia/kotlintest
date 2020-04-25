import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import util.SomeTestUtil

object SomeTest : Spek({

	describe("an example test") {

		it("should succeed") {

			"hello" shouldBeEqualTo "hello"
			"hello" shouldNotBeEqualTo "world"

			// Accessible via the project dependency
			SomeUtil.doSomething()

			// Accessible in this project because of a testFixtures dependency
			SomeTestUtil.doSomething()
			//SomeLocalTest.doSomething() // not visible
		}
	}

})
