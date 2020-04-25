import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SomeOtherTest : Spek({

	describe("an example test") {

		it("should succeed") {

			"hello" shouldBeEqualTo "hello"
			"hello" shouldNotBeEqualTo "world"

			// Accessible via the project dependency
			SomeUtil.doSomething()

			//SomeTestUtil.doSomething() // not visible because no testFixtures dependency
			//SomeLocalTest.doSomething() // not visible
		}
	}

})
