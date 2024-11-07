class cartaMonstro {
    // Classe para a carta monstro
    class CartaMonstro(
        nome: String,
        descricao: String,
        ataque: Int,
        defesa: Int,
        private var state: Monstrostate = Monstrostate.DEFESA // Estado inicial
    ) : Carta(nome, descricao, ataque, defesa) { // herda da classe Carta

        fun poder(): Int {
            return if (state == Monstrostate.ATAQUE)
                ataque
            else
                defesa
        }

        fun alterarState() {
            state = if (state == Monstrostate.ATAQUE)
                Monstrostate.DEFESA  // Altera para defesa
            else
                Monstrostate.ATAQUE  // Altera para ataque
        }

        fun modoAtaque(): Boolean {
            return state == Monstrostate.ATAQUE // retorna estado de ataque
        }

        override fun mostrarInfo(): String {
            return "Monstro: ${getNome()} | Ataque: $ataque | Defesa: $defesa | Estado: $state"

        }

    }

    enum class Monstrostate {
        ATAQUE, DEFESA
    }

}