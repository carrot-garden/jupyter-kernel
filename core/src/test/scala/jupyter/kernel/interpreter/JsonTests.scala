package jupyter.kernel.interpreter

import jupyter.kernel.protocol.Formats._
import jupyter.kernel.protocol.Output._

import argonaut._, Argonaut._, Shapeless._

import utest._

object JsonTests extends TestSuite {

  val tests = TestSuite {
    'reply - {
      'statusField - {
        val okReply = ExecuteOkReply(3)
        val errorReply = ExecuteErrorReply(4, "name", "value", List("t1", "t2"))
        val abortReply = ExecuteAbortReply(5)

        def statusOf(json: Json): Option[String] = {
          case class WithStatus(status: String)
          json.asJson.as[WithStatus].toOption.map(_.status)
        }

        val okStatusOpt = statusOf(okReply.asJson)
        val errorStatusOpt = statusOf(errorReply.asJson)
        val abortStatusOpt = statusOf(abortReply.asJson)

        assert(okStatusOpt == Some("ok"))
        assert(errorStatusOpt == Some("error"))
        assert(abortStatusOpt == Some("abort"))
      }
    }

    'objectInfo - {
      'foundField - {
        val foundReply = ObjectInfoFoundReply("obj")
        val notFoundReply = ObjectInfoNotFoundReply("obj0")

        def foundFieldOf(json: Json): Option[Boolean] = {
          case class WithFoundField(found: Boolean)
          json.asJson.as[WithFoundField].toOption.map(_.found)
        }

        val foundReplyFieldOpt = foundFieldOf(foundReply.asJson)
        val notFoundReplyFieldOpt = foundFieldOf(notFoundReply.asJson)

        assert(foundReplyFieldOpt == Some(true))
        assert(notFoundReplyFieldOpt == Some(false))
      }
    }
  }

}
