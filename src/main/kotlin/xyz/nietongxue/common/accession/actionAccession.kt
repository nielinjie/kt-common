package xyz.nietongxue.common.accession

import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.log.Log
import xyz.nietongxue.common.processing.Action
import xyz.nietongxue.common.processing.StopResult
import xyz.nietongxue.common.processing.bind
import xyz.nietongxue.common.processing.flatMap

class ActionAccession<T, L>(val actions: List<Action<T, L>>, id: Id, base: Id?, reason: List<Id>) :
    Accession(id, base, reason)

class ActionAccessionBranch<T, L>(override val zero: Zero<T>) : AccessionBranch<T, ActionAccession<T, L>>() {
    override fun snapshot(beforeOrEqualTo: Id? ): T {
        val re = this.oldToNew(beforeOrEqualTo) //TODO apply beforeOrEqualTo
            .fold(bind<Log<L>, StopResult<L>, T>(zero.zero)) { acc, accession ->
                accession.actions.fold(acc) { acc2, action ->
                    acc2.flatMap { value ->
                        action.action()(value)
                    }
                }
            }
        return re.second.getOrNull() ?: error("should not be null")
    }
}