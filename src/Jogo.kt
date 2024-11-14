import kotlin.random.Random

class Jogo(private val colecaoDeCartas: List<Carta>) {
    private val jogadores: List<Jogador>
    private var rodada: Int = 0
    private var jogoFinalizado = false

    init {
        jogadores = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
        distribuirCartasIniciais()
    }

    protected fun distribuirCartasIniciais() {
        jogadores.forEach { jogador ->
            repeat(5) {
                val carta = colecaoDeCartas.random()
                jogador.receberCarta(carta)
            }
        }
    }

    fun iniciarJogo() {
        rodada = 1
        while (jogadores.any { it.pontosDeVida > 0 } && colecaoDeCartas.isNotEmpty()) {
            println("\n------------------------------------------------------")
            println("\nRodada $rodada:")
            iniciarRodada()
            rodada++
        }
        verificarVencedor()
    }

    protected fun iniciarRodada() {
        jogadores.forEach { jogador ->
            if (jogador.mao.size < 10 && colecaoDeCartas.isNotEmpty()) {
                val novaCarta = colecaoDeCartas.random()
                jogador.receberCarta(novaCarta)
            }


            println("\n------------------------------------------------------")
            println("Bem-vindo(a) aventureiro(a) ao jogo de Cartas Monstro!!!")
            println("Escolhas do ${jogador.nome}:")
            jogador.escolherAcao(this)
        }
    }

    fun realizarAcaoJogador(jogador: Jogador, acao: String, carta: Carta? = null, alvo: CartaMonstro? = null) {
        when (acao) {
            "posicionar" -> if (carta is CartaMonstro) jogador.posicionarMonstro(carta)
            "equipar" -> if (carta is CartaEquipamento && alvo != null) jogador.equiparMonstro(alvo, carta)
            "descartar" -> if (carta != null) jogador.descartarCarta(carta)
            "atacar" -> if (alvo != null && carta is CartaMonstro) atacar(jogador, carta, alvo)
            "alterar" -> if (carta is CartaMonstro) carta.alternarEstado()
        }
    }

    protected fun atacar(jogador: Jogador, atacante: CartaMonstro, alvo: CartaMonstro) {
        if (atacante.estado != MonstroState.ATAQUE) {
            println("Somente monstros em estado de ataque podem atacar.")
            return
        }

        val oponente = jogadores.first { it != jogador }

        if (alvo.estado == MonstroState.ATAQUE) {
            val dano = atacante.ataque - alvo.ataque
            if (dano > 0) {
                oponente.pontosDeVida -= dano
                println("${jogador.nome} causou $dano pontos de dano a ${oponente.nome}.")
            } else {
                jogador.pontosDeVida += dano  // dano é negativo, então subtrai pontos de vida
                println("${jogador.nome} perdeu ${-dano} pontos de vida.")
            }
        } else {
            if (atacante.ataque > alvo.defesa) {
                oponente.tabuleiro.remove(alvo)
                println("${alvo.nome} foi destruído.")
            } else {
                val dano = alvo.defesa - atacante.ataque
                jogador.pontosDeVida -= dano
                println("${jogador.nome} perdeu $dano pontos de vida.")
            }
        }
    }

    protected fun verificarVencedor() {
        val jogadorVivo = jogadores.filter { it.pontosDeVida > 0 }
        when {
            jogadorVivo.size == 1 -> {
                println("${jogadorVivo[0].nome} venceu o jogo!")
                jogoFinalizado = true
            }

            jogadorVivo.isEmpty() -> {
                println("Empate! Ambos os jogadores perderam todos os pontos de vida.")
                jogoFinalizado = true
            }

            else -> {
                val vencedor = jogadores.maxByOrNull { it.pontosDeVida }
                println("${vencedor?.nome} venceu o jogo com ${vencedor?.pontosDeVida} pontos de vida!")
                jogoFinalizado = true
            }
        }
    }

    // Função para finalizar o jogo
    public fun finalizarJogo() {
        println("Jogo finalizado.")
        jogoFinalizado = true
    }
}
