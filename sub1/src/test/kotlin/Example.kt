import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class Example : Spek({

    describe("an example test") {

        it("should succeed") {

            "hello" shouldEqual "hello"
            "hello" shouldNotEqual "world"

        }
    }

});
