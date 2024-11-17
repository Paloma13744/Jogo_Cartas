import kotlin.random.Random

class Jogo(private val colecaoDeCartas: List<Carta>) {
    private val jogadores: List<Jogador>
    private var rodada: Int = 0
    private var jogoFinalizado = false
    private var vencedor = false

    init {  // Inicializa o jogo com 2 jogadores e distrbui as cartas
        jogadores = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
        distribuirCartasIniciais()
    }

    protected fun distribuirCartasIniciais() {  // Função responsável por distribuir as cartas iniciais para cada jogador.
        jogadores.forEach { jogador ->
            repeat(5) {
                val carta = colecaoDeCartas.random()
                jogador.receberCarta(carta)

            }
        }
    }

    fun iniciarJogo() {
        jogadores.forEach { it.equipamentoUsado = false }
        rodada = 1
        while (jogadores.any { it.pontosDeVida > 0 } && colecaoDeCartas.isNotEmpty()) {
            iniciarRodada()
            rodada++
            if (jogoFinalizado) return // Se o jogo terminar, sai da função.
        }
        verificarVencedor()
    }

    protected fun iniciarRodada() {

        jogadores.forEach { jogador -> jogador.resetarEstadoMonstros()}

        jogadores.forEach { it.equipamentoUsado = false }

        jogadores.forEach{ it.isEquipol = false}

        if (rodada == 1) {
            jogadores.forEach { it.primeiraRodada = true }  // Na primeira rodada, é verdadeiro
        } else {
            jogadores.forEach { it.primeiraRodada = false }

        }

        // Reseta a variável podePosicionarMonstro para todos os jogadores no início de cada rodada
        jogadores.forEach { it.podePosicionarMonstro = true }

        // Verifica se o jogo já foi finalizado antes de começar a rodada
        if (jogoFinalizado) return  // Se o jogo já terminou, sai da função

        // Exibe uma mensagem de boas-vindas na primeira rodada
        println("\n------------------------------------------------------------------------")

        if (rodada == 1) {
            println("\u001B[31m\u001B[3mBem-vindo(a) aventureiro(a) ao jogo de Cartas Monstro!!!\u001B[0m")
        }
        println("Iniciando a Rodada $rodada:")
        // Cada jogador escolhe a sua ação para a rodada
        jogadores.forEach { jogador ->
            // Se o jogador já perdeu, o jogo é finalizado antes de sua vez
            if (jogador.pontosDeVida <= 0) {
                println("${jogador.nome} já perdeu todos os pontos de vida! O jogo acabou.")
                jogoFinalizado = true
                return
            }
            if ( colecaoDeCartas.isNotEmpty()) {
                // Distribui uma nova carta aleatória da coleção para o jogador
                val novaCarta = colecaoDeCartas.random()
                jogador.receberCarta(novaCarta)
            }

            if (jogoFinalizado) return // Verifica se o jogo foi finalizado durante a rodada

            // Chama a função para realizar a ação do jogador
            println("\nEscolhas do ${jogador.nome}:")
            jogador.escolherAcao(this) // Aqui, o jogador escolhe a ação, que será processada em realizarAcaoJogador
        }

        // Verifica se algum jogador perdeu, e se sim, finaliza o jogo
        verificarVencedor()  // Verifica se algum jogador venceu ou perdeu após a rodada

  }


    // Função para obter o oponente de um jogador
    fun obterOponente(jogador: Jogador): Jogador? {
        return if (jogador == jogadores[0]) jogadores[1] else jogadores[0]
    }

    fun verificarVencedor() {
        val jogadoresComVidaPositiva = jogadores.filter { it.pontosDeVida > 0 }
        val jogadoresComVidaZeroOuMenor = jogadores.filter { it.pontosDeVida <= 0 }

        when {
            // Verifica se há apenas um jogador com vida positiva e o outro com vida menor ou igual a zero
            jogadoresComVidaPositiva.size == 1 && jogadoresComVidaZeroOuMenor.size == 1 -> {
                val vencedor = jogadoresComVidaPositiva[0]
                println("${vencedor.nome} 🏆 venceu o jogo com ${vencedor.pontosDeVida} pontos de vida!")
                jogoFinalizado = true  // Marca o jogo como finalizado
            }

            // Caso ainda não tenha um vencedor claro, mas algum jogador perdeu
            jogadoresComVidaZeroOuMenor.size > 0 -> {
                val jogadorPerdedor = jogadoresComVidaZeroOuMenor[0]
                println("${jogadorPerdedor.nome} perdeu todos os pontos de vida. O jogo acabou!")
                jogoFinalizado = true  // Marca o jogo como finalizado
            }

            // Caso ambos os jogadores tenham perdido todos os pontos de vida
            jogadoresComVidaZeroOuMenor.size == jogadores.size -> {
                println("Empate! Ambos os jogadores perderam todos os pontos de vida.")
                jogoFinalizado = true  // Marca o jogo como finalizado
            }

            else -> {
                println("Não há vencedor claro no momento.")
            }
        }
    }


    // Função para finalizar o jogo quando um jogador pede para sair
    public fun finalizarJogo(jogadorQueSaiu: Jogador) {
        if (jogoFinalizado) {
            println("O jogo já foi finalizado.")
            return
        }

        val oponente = obterOponente(jogadorQueSaiu)

        if (oponente != null) {
            println("${jogadorQueSaiu.nome} pediu para sair do jogo!")
            println("${oponente.nome} é o vencedor com ${oponente.pontosDeVida} pontos de vida!")
        } else {
            println("${jogadorQueSaiu.nome} pediu para sair do jogo, mas nenhum oponente foi encontrado.")
        }

        jogoFinalizado = true // Marca o jogo como finalizado.
    }

}