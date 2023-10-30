package xyz.nietongxue.common.processing


interface AssemblePhase {
    object Finish : AssemblePhase
}


open class Assemble<V, L>(value: V, val rules: List<Rule<V, L>>) {
    private var current: ProcessingWithLog<V, L> = bind(value)
    fun apply(vararg actions:Action<V,L>): Assemble<V, L> {
        actions.forEach {
            innerApply(it)
        }
        return this
    }
    private fun innerApply(action: Action<V,L>): Assemble<V, L> {
        current = current.flatMap {
            action.action()(it)
        }
        return this
    }

    fun finish(): ProcessingWithLog<V, L> {
        var re = current
        rules.forEach { rule ->
            re = re.flatMap {
                rule.check(it, AssemblePhase.Finish)
            }
        }
        if (re.second.isRight()) {
            this.current = re
        }
        return re
    }


}

interface Rule<V, L> {
    fun check(value: V, phase: AssemblePhase): ProcessingWithLog<V, L>
}