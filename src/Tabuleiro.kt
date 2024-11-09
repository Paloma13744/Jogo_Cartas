class Tabuleiro {
    private val tabuleiro = mutableListOf<CartaMonstro>()
    private val tamMaxTabuleiro = 5 // Limite de monstros no tabuleiro

    // Adiciona um monstro ao tabuleiro
    fun adicionarMonstro(monstro: CartaMonstro): Boolean {
        return if (tabuleiro.size < tamMaxTabuleiro) {
            tabuleiro.add(monstro)
            println("${monstro.obterNome()} foi colocado no tabuleiro.")
            true
        } else {
            println("Tabuleiro cheio. Não é possível adicionar mais monstros.")
            false
        }
    }

    // Realiza um ataque entre dois monstros no tabuleiro
    fun realizarAtaque(monstroAtacante: CartaMonstro, monstroOponente: CartaMonstro) {
        val dano = monstroAtacante.poder() - monstroOponente.poder()
        if (dano > 0) {
            println("${monstroAtacante.obterNome()} causou $dano pontos de dano em ${monstroOponente.obterNome()}.")
            monstroOponente.reduzirVida(dano)
        } else {
            println("${monstroOponente.obterNome()} se defendeu do ataque.")
        }
    }

    // Atacar com um monstro do tabuleiro
    fun atacarComMonstro(indiceAtacante: Int, indiceOponente: Int): Boolean {
        if (indiceAtacante in 0 until tabuleiro.size && indiceOponente in 0 until tabuleiro.size) {
            val monstroAtacante = tabuleiro[indiceAtacante]
            val monstroOponente = tabuleiro[indiceOponente]
            realizarAtaque(monstroAtacante, monstroOponente)
            return true
        }
        println("Índices inválidos para ataque.")
        return false
    }

    // Alterar o estado de um monstro no tabuleiro
    fun alterarEstadoMonstro(indice: Int) {
        if (indice in 0 until tabuleiro.size) {
            val monstro = tabuleiro[indice]
            monstro.alterarEstado()
            println("${monstro.obterNome()} agora está em estado ${monstro.modoAtaque()}")
        } else {
            println("Índice inválido para alterar o estado do monstro.")
        }
    }

    fun perderPontosDeVida(indice: Int, dano: Int) {
        if (indice in 0 until tabuleiro.size) {
            val monstro = tabuleiro[indice]
            monstro.reduzirVida(dano)
        } else {
            println("Índice inválido para perder pontos de vida.")
        }
    }
    // Retorna todos os monstros no tabuleiro
    fun getMonstros(): List<CartaMonstro> = tabuleiro

    // Retorna se o tabuleiro está vazio ou não
    fun isVazio(): Boolean = tabuleiro.isEmpty()
}
