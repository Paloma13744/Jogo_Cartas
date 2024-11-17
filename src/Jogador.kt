import kotlin.random.Random

class Jogador(val nome: String) {
    val mao: MutableList<Carta> = mutableListOf() // Cartas do jogador
    val tabuleiro: MutableList<CartaMonstro?> = MutableList(5) { null }// Monstros no tabuleiro
    var pontosDeVida = 10000 // Pontos iniciais
    private var tituloExibido = false
    var podePosicionarMonstro = true
    var equipamentoUsado: Boolean = false
    var primeiraRodada: Boolean = true
    var isEquipol: Boolean = false
    var jaAtacou: Boolean = false


    fun exibirPontosDeVida() { // Função para exibir os pontos de vida do jogador
        println("$nome, seus pontos de vida atuais são: $pontosDeVida")
    }

    fun receberCarta(carta: Carta) {  // Função para o jogador receber uma carta
        if (!tituloExibido) {
            println("\u001B[31m\u001B[3m |\uD83D\uDC7B|  Jogo de Cartas Monstro  |\uD83D\uDC09|\u001B[0m")
            tituloExibido = true
            print("\n")
        }
        mao.add(carta)// Adiciona a carta à mão do jogador
        val tipo = when (carta) { // Verifica o tipo de carta e mostra ao jogador o tipo de carta recebida
            is CartaEquipamento -> {
                println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.bonusAtaque}, Defesa: ${carta.bonusDefesa})")
                "Equipamento"
            }

            is CartaMonstro -> {
                println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
                "Monstro"
            }

            else -> "Desconhecido"
        }
    }

    fun posicionarMonstro() { // Função para o jogador posicionar um monstro no tabuleiro
        if (!podePosicionarMonstro) {
            println("Você não pode posicionar monstros nesta rodada.")
            return
        }

        val monstros = mao.filterIsInstance<CartaMonstro>()

        // Verifica se o tabuleiro já está cheio
        if (tabuleiro.none { it == null }) {
            println("O tabuleiro está cheio! Não é possível posicionar mais monstros.")
            return
        }

        // Rodada de dados antes de posicionar o primeiro monstro
        println("Antes de posicionar um monstro, você precisa rodar o dado de 21 lados.")
        val dado = (1..21).random()
        println("Você rolou o dado e tirou: $dado")


        // Se o dado for maior que 17, o jogador pode posicionar o primeiro e o segundo monstro
        if (dado > 17) {
            println("Você conseguiu tirar um número maior que 17! Agora, pode posicionar o primeiro monstro.")
            podePosicionarMonstro = true
        } else {
            println("Você não conseguiu tirar um número maior que 17. Só poderá posicionar o primeiro monstro.")
            println("\n------------------------------------------------------------------------")
            podePosicionarMonstro = false // Trava a opção de posicionar o segundo monstro
        }

        // Posicionamento do primeiro monstro
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro da sua mão para posicionar:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} : ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
            }

            var monstroEscolhido: CartaMonstro? = null
            while (monstroEscolhido == null) {
                val escolha = readLine()?.toIntOrNull()
                if (escolha != null && escolha in 1..monstros.size) {
                    monstroEscolhido = monstros[escolha - 1]
                } else {
                    println("Opção inválida! Por favor, escolha um número entre 1 e ${monstros.size}.")
                }
            }

            // Pergunta ao jogador em qual estado ele deseja colocar o monstro
            var estadoEscolhido: Int? = null
            while (estadoEscolhido !in 1..2) {
                println("Escolha o estado do monstro ${monstroEscolhido.nome}:")
                println("\u001B[31m1) Ataque\u001B[0m")
                println("\u001B[92m2) Defesa\u001B[0m")
                estadoEscolhido = readLine()?.toIntOrNull()

                when (estadoEscolhido) {
                    1 -> monstroEscolhido.estado = MonstroState.ATAQUE
                    2 -> monstroEscolhido.estado = MonstroState.DEFESA
                    else -> {
                        println("Opção inválida! Por favor, escolha 1 para Ataque ou 2 para Defesa.")
                    }
                }
            }
            // Exibe os espaços livres no tabuleiro
            val espacosLivres = tabuleiro.mapIndexedNotNull { index, carta -> if (carta == null) index + 1 else null }
            // Loop para garantir uma entrada válida
            var posicaoEscolhida: Int? = null
            while (posicaoEscolhida == null || posicaoEscolhida !in espacosLivres) {
                println(
                    "Escolha uma posição para colocar o monstro (escolha entre os espaços livres: ${
                        espacosLivres.joinToString(
                            ", "
                        )
                    }):"
                )
                posicaoEscolhida = readLine()?.toIntOrNull()

                // Verifica se a entrada é inválida
                if (posicaoEscolhida == null) {
                    println("Entrada inválida! Por favor, digite um número.")
                } else if (posicaoEscolhida !in 1..5) {
                    println("Posição fora do intervalo! Escolha um número entre 1 e 5.")
                } else if (posicaoEscolhida !in espacosLivres) {
                    println(
                        "Essa posição já está ocupada ou não é válida! Escolha uma das posições livres: ${
                            espacosLivres.joinToString(
                                ", "
                            )
                        }."
                    )
                }
            }

            // Posiciona o monstro na posição escolhida no tabuleiro
            tabuleiro[posicaoEscolhida - 1] = monstroEscolhido // Posiciona o monstro na posição escolhida
            mao.remove(monstroEscolhido) // Remove o monstro da mão
            println("${nome} posicionou o monstro ${monstroEscolhido.nome} na posição $posicaoEscolhida do tabuleiro em estado ${monstroEscolhido.estado}.")
        } else {
            println("Você não tem monstros na sua mão!")
            return
        }

        // Se o dado foi maior que 17, tenta posicionar o segundo monstro
        if (dado > 17 && monstros.isNotEmpty()) {
            val monstrosDisponiveis = mao.filterIsInstance<CartaMonstro>()

            println("Agora você pode tentar posicionar o segundo monstro.")
            if (monstrosDisponiveis.isNotEmpty()) {
                println("Escolha um monstro da sua mão para posicionar:")
                monstrosDisponiveis.forEachIndexed { index, carta ->
                    println("${index + 1}) ${carta.nome} : ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
                }

                var segundoMonstroEscolhido: CartaMonstro? = null
                while (segundoMonstroEscolhido == null) {
                    val escolha = readLine()?.toIntOrNull()
                    if (escolha != null && escolha in 1..monstrosDisponiveis.size) {
                        segundoMonstroEscolhido = monstrosDisponiveis[escolha - 1]
                    } else {
                        println("Opção inválida! Por favor, escolha um número entre 1 e ${monstrosDisponiveis.size}.")
                    }
                }

                // Pergunta ao jogador em qual estado ele deseja colocar o monstro
                var estadoEscolhido: Int? = null
                while (estadoEscolhido !in 1..2) {
                    println("Escolha o estado do segundo monstro ${segundoMonstroEscolhido.nome}:")
                    println("1) Ataque")
                    println("2) Defesa")
                    estadoEscolhido = readLine()?.toIntOrNull()

                    when (estadoEscolhido) {
                        1 -> segundoMonstroEscolhido.estado = MonstroState.ATAQUE
                        2 -> segundoMonstroEscolhido.estado = MonstroState.DEFESA
                        else -> {
                            println("Opção inválida! Por favor, escolha 1 para Ataque ou 2 para Defesa.")
                        }
                    }
                }

                // Atualiza os espaços livres antes de posicionar o segundo monstro
                val espacosLivres =
                    tabuleiro.mapIndexedNotNull { index, carta -> if (carta == null) index + 1 else null }

                // Escolha a posição para o segundo monstro
                var posicaoEscolhida: Int? = null
                while (posicaoEscolhida == null || posicaoEscolhida !in espacosLivres) {
                    println(
                        "Escolha uma posição para colocar o segundo monstro (escolha entre os espaços livres: ${
                            espacosLivres.joinToString(
                                ", "
                            )
                        }):"
                    )
                    posicaoEscolhida = readLine()?.toIntOrNull()

                    if (posicaoEscolhida == null) {
                        println("Entrada inválida! Por favor, digite um número.")
                    } else if (posicaoEscolhida !in 1..5) {
                        println("Posição fora do intervalo! Escolha um número entre 1 e 5.")
                    } else if (posicaoEscolhida !in espacosLivres) {
                        println(
                            "Essa posição já está ocupada ou não é válida! Escolha uma das posições livres: ${
                                espacosLivres.joinToString(
                                    ", "
                                )
                            }."
                        )
                    }
                }

                // Posiciona o segundo monstro no tabuleiro
                tabuleiro[posicaoEscolhida - 1] = segundoMonstroEscolhido // Usa a posição válida escolhida
                mao.remove(segundoMonstroEscolhido) // Remove o monstro da mão
                println("${nome} posicionou o segundo monstro ${segundoMonstroEscolhido.nome} na posição $posicaoEscolhida do tabuleiro em estado ${segundoMonstroEscolhido.estado}.")
            } else {
                println("Você não tem mais monstros na mão para posicionar!")
            }
        } else {
            println("Você não pode posicionar o segundo monstro nesta rodada.")
            println("\n------------------------------------------------------------------------")

        }
    }

    fun escolherMonstro(monstros: List<CartaMonstro>): CartaMonstro? {
        println("Escolha um monstro da sua mão para posicionar:")
        monstros.forEachIndexed { index, carta ->
            println("${index + 1}) ${carta.nome} : ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})")
        }
        val escolha = readLine()?.toIntOrNull()
        return monstros.getOrNull(escolha?.minus(1) ?: -1)
    }

    fun escolherEstado(monstro: CartaMonstro): MonstroState {
        println("Escolha o estado do monstro ${monstro.nome}:")
        println("1) Ataque")
        println("2) Defesa")
        var estadoEscolhido: Int?
        do {
            estadoEscolhido = readLine()?.toIntOrNull()
            if (estadoEscolhido != 1 && estadoEscolhido != 2) {
                println("Opção inválida! Por favor, escolha 1 para Ataque ou 2 para Defesa.")
            }
        } while (estadoEscolhido != 1 && estadoEscolhido != 2)

        return if (estadoEscolhido == 1) MonstroState.ATAQUE else MonstroState.DEFESA
    }

    fun escolherPosicao(): Int {
        println("Escolha uma posição para colocar o monstro (1 a 5):")
        var posicaoEscolhida: Int?
        do {
            posicaoEscolhida = readLine()?.toIntOrNull()
            if (posicaoEscolhida == null || posicaoEscolhida !in 1..5 || tabuleiro[posicaoEscolhida - 1] != null) {
                println("Posição inválida ou já ocupada! Escolha uma posição livre de 1 a 5.")
            }
        } while (posicaoEscolhida == null || posicaoEscolhida !in 1..5 || tabuleiro[posicaoEscolhida - 1] != null)
        return posicaoEscolhida
    }

    fun realizarAtaque(jogadorAdversario: Jogador) {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        // Verifique se é a primeira rodada e bloqueie o ataque
        if (primeiraRodada == true) {
            println("Você não pode atacar na primeira rodada.")
            return
        }

        if (monstros.isNotEmpty()) {
            // Mostra os monstros que o jogador pode escolher para atacar
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa}, Já atacou: ${carta.jaAtacou}, Estado: ${carta.estado})")
            }

            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Verifica se o monstro já atacou
                if (monstroEscolhido.jaAtacou) {
                    println("Este monstro já atacou nesta rodada e não pode atacar novamente.")
                    return
                }

                // Escolhe um monstro adversário para atacar
                val monstrosAdversarios = jogadorAdversario.tabuleiro.filterIsInstance<CartaMonstro>()
                if (monstrosAdversarios.isNotEmpty()) {
                    println("Escolha um monstro adversário para atacar:")
                    monstrosAdversarios.forEachIndexed { index, carta ->
                        println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa}, Estado: ${carta.estado})")
                    }

                    // Lê a escolha do monstro adversário
                    val escolhaMonstroAdversario = readLine()?.toIntOrNull()
                    val monstroAdversarioEscolhido =
                        monstrosAdversarios.getOrNull(escolhaMonstroAdversario?.minus(1) ?: -1)

                    if (monstroAdversarioEscolhido != null) {
                        // Verifica o estado do monstro adversário
                        if (monstroAdversarioEscolhido.estado == MonstroState.DEFESA) {
                            // Ataque contra monstro em DEFESA
                            if (monstroEscolhido.ataque > monstroAdversarioEscolhido.defesa) {
                                println("${monstroAdversarioEscolhido.nome} foi destruído!")
                                jogadorAdversario.tabuleiro.remove(monstroAdversarioEscolhido)
                                val danoRecebido = monstroEscolhido.ataque - monstroAdversarioEscolhido.defesa
                                // O adversário perde pontos de vida
                                jogadorAdversario.pontosDeVida -= danoRecebido
                                println("${jogadorAdversario.nome} perdeu $danoRecebido pontos de vida!")
                            } else {
                                val danoAoAtacante =
                                    (monstroAdversarioEscolhido.defesa - monstroEscolhido.ataque).coerceAtLeast(0)
                                pontosDeVida -= danoAoAtacante
                                println("O ataque falhou! ${monstroEscolhido.nome} sofreu $danoAoAtacante de dano!")
                            }
                        } else {
                            // Ataque contra monstro em ATAQUE
                            val dano = (monstroEscolhido.ataque - monstroAdversarioEscolhido.ataque).coerceAtLeast(0)
                            if (dano > 0) {
                                println("${monstroEscolhido.nome} atacou ${monstroAdversarioEscolhido.nome}!")
                                jogadorAdversario.pontosDeVida -= dano
                                println("${jogadorAdversario.nome} perdeu $dano pontos de vida!")
                            } else {
                                println("O ataque foi bloqueado pelo monstro adversário!")
                            }
                        }
                    } else {
                        println("Monstro adversário inválido para atacar.")
                    }
                } else {
                    // Ataque direto se não houver monstros adversários
                    println("O adversário não tem monstros para atacar, então recebeu ataque direto!")
                    val dano = monstroEscolhido.ataque
                    jogadorAdversario.pontosDeVida -= dano
                    println("${jogadorAdversario.nome} perdeu $dano pontos de vida!")
                    mostrarPontuacaoAtual(jogadorAdversario)
                }

                // Marcar que o monstro já atacou nesta rodada
                monstroEscolhido.jaAtacou = true
            } else {
                println("Monstro inválido para atacar.")
            }
        } else {
            println("Você não tem monstros para atacar!")
        }
    }

    fun equiparMonstro() {     // Função para equipar um monstro com um equipamento
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        val equipamentos = mao.filterIsInstance<CartaEquipamento>()
        if (isEquipol != true) {
            if (monstros.isNotEmpty() && equipamentos.isNotEmpty()) {
                println("Escolha um monstro para equipar:")
                monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") }
                val escolhaMonstro = readLine()?.toIntOrNull()
                val monstroEscolhido = monstros.getOrNull(escolhaMonstro?.minus(1) ?: -1)

                if (monstroEscolhido != null) {
                    println("Escolha um equipamento para equipar:")
                    equipamentos.forEachIndexed { index, carta ->
                        println("${index + 1}) ${carta.nome} (Ataque: +${carta.bonusAtaque}, Defesa: +${carta.bonusDefesa})")
                    }
                    val escolhaEquipamento = readLine()?.toIntOrNull()
                    val equipamentoEscolhido = equipamentos.getOrNull(escolhaEquipamento?.minus(1) ?: -1)

                    if (equipamentoEscolhido != null) {
                        // Aplica os bônus do equipamento ao monstro
                        monstroEscolhido.ataque += equipamentoEscolhido.bonusAtaque
                        monstroEscolhido.defesa += equipamentoEscolhido.bonusDefesa

                        // Remove o equipamento da mão do jogador
                        mao.remove(equipamentoEscolhido)
                        isEquipol = true

                        println("$nome equipou ${monstroEscolhido.nome} com ${equipamentoEscolhido.nome}!")
                        println("${monstroEscolhido.nome} agora tem Ataque: ${monstroEscolhido.ataque} e Defesa: ${monstroEscolhido.defesa}")
                    } else {
                        println("Equipamento inválido.")
                    }
                } else {
                    println("Monstro inválido.")
                }
            } else {

                println("Equipamentos indisponíveis!")


            }

        } else {
            println("Você só pode equipar um item por rodada")

        }
    }

    fun finalizarVez() {
        return
    }

    fun alterarEstadoMonstro() { // Função para alterar o estado de um monstro no tabuleiro
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para alterar o estado:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} - Estado atual: ${carta.estado} - Já atacou: ${carta.jaAtacou}")
            }

            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                if (monstroEscolhido.jaAtacou == true) {
                    println("Este monstro já atacou nesta rodada, não é possível alterar o estado dele.")
                    return
                }

                // Exibe o estado atual do monstro antes de alterar
                println("Estado atual de ${monstroEscolhido.nome}: ${monstroEscolhido.estado}")

                // Alterna o estado do monstro
                monstroEscolhido.alternarEstado()

                // Exibe o novo estado após a alteração
                println("O estado de ${monstroEscolhido.nome} foi alterado para: ${monstroEscolhido.estado}")
            } else {
                println("Monstro inválido para alterar estado.")
            }
        } else {
            println("Você não tem monstros para alterar o estado!")
        }
    }

    // Função para descartar cartas quando o jogador tem mais de 10 cartas na mão
    fun descartarCarta(jogador: Jogador) {
        // Exibe as cartas que o jogador tem
        jogador.mao.forEachIndexed { index, carta ->
            // Verifica o tipo da carta e exibe as informações correspondentes
            when (carta) {
                is CartaMonstro -> {
                    // Se a carta for do tipo CartaMonstro, exibe nome, ataque e defesa
                    println("${index + 1}) ${carta.nome} (Tipo: Monstro) - Ataque: ${carta.ataque}, Defesa: ${carta.defesa}")
                }

                is CartaEquipamento -> {
                    // Se a carta for do tipo CartaEquipamento, exibe nome, ataque bônus e defesa bônus
                    println("${index + 1}) ${carta.nome} (Tipo: Equipamento) - Ataque Bônus: ${carta.bonusAtaque}, Defesa Bônus: ${carta.bonusDefesa}")
                }

                else -> {
                    // Para outros tipos de cartas (caso haja), pode mostrar uma mensagem padrão
                    println("${index + 1}) ${carta.nome} (Tipo Desconhecido)")
                }
            }
        }


        // Lê a escolha do jogador para descartar uma carta
        val escolhaDescartar = readLine()?.toIntOrNull()

        // Verifica se a escolha é válida
        if (escolhaDescartar != null && escolhaDescartar in 1..jogador.mao.size) {
            val cartaDescartada = jogador.mao.removeAt(escolhaDescartar - 1) // Remove a carta escolhida
            println("Você descartou a carta: ${cartaDescartada.nome}")
        } else {
            println("Escolha inválida. Nenhuma carta foi descartada.")
        }
    }

    fun verificarLimiteDeCartas(jogador: Jogador) {
        // Verifica se o jogador tem exatamente 10 cartas
        if (jogador.mao.size > 10) {
            println("${jogador.nome}, você tem 11 cartas na sua mão! Escolha uma carta para descartar.")
            descartarCarta(jogador)  // Chama a função de descarte
        }
    }

    fun resetarEstadoMonstros() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        if (monstros.isNotEmpty()) {
            monstros.forEach { monstro ->
                monstro.jaAtacou = false
            }
            println("O estado de ataque de todos os monstros foi resetado para a nova rodada.")
        }

    }


    fun resetarAtaques() { // Muda o estado do monstro para defesa
        tabuleiro.forEach {
            if (it != null) {
                it.estado = MonstroState.DEFESA
            }
        }
    }

    fun mostrarPontuacaoAtual(jogadorAdversario: Jogador) {  //  pontos de vida de cada jogador
        println("${this.nome}, sua pontuação de vida atual é: $pontosDeVida")
        println("${jogadorAdversario.nome}, a pontuação de vida atual é: ${jogadorAdversario.pontosDeVida}")
    }


    fun escolherAcao(jogo: Jogo) {  // Menu interativo para os jogadores
        var continuarJogando = true  // Flag para controlar o loop

        while (continuarJogando) {  // O loop continua enquanto não houver vencedor
            println("\u001B[31m\u001B[3m\uD83D\uDCDC Menu de escolhas dos jogadores:\u001B[0m")
            println("1)\uD83D\uDC09 Posicionar um novo monstro no tabuleiro")
            println("2)⚙️ Equipar um monstro com uma carta de equipamento")
            println("3)\uD83D\uDDD1️ Descartar uma carta da mão")
            println("4)⚔️ Realizar um ataque contra o oponente")
            println("5)\uD83D\uDD04 Alterar o estado de um monstro (ataque/defesa)")
            println("6)⏹\uFE0F Finalizar ação")
            println("7)\uD83D\uDC4B Sair")
            println("8)❤\uFE0F Exibir vida")
            print("\n")
            println("\u001B[31m\u001B[3m\uD83C\uDFAE $nome, selecione uma opção:\u001B[0m")

            val opcao = readLine()
                ?.trim()           // Remove espaços no início e fim
                ?.replace(Regex("[,.;]"), "")  // Remove pontos, vírgulas e ponto e vírgula
                ?.toIntOrNull()

            if (opcao == null || opcao !in 1..8) {
                println("Opção inválida. Por favor, escolha uma opção de 1 a 8.")
                continue // Pula para a próxima iteração
            }

            when (opcao) {

                1 -> posicionarMonstro()
                2 -> equiparMonstro()
                3 -> descartarCarta(this)
                4 -> {
                    val oponente = jogo.obterOponente(this)
                    if (oponente != null) {
                        realizarAtaque(oponente)
                    } else {
                        println("Não foi possível encontrar um oponente!")
                    }
                }

                5 -> alterarEstadoMonstro()
                6 -> {
                    finalizarVez()
                    continuarJogando = false // Encerra o loop após finalizar a vez
                    println("Vez Finalizada")
                }

                7 -> {
                    println("Saindo do jogo... O jogo terminou!")
                    jogo.finalizarJogo(this)
                    continuarJogando = false // Encerra o loop se o jogador escolher sair
                }

                8 -> {
                    exibirPontosDeVida()
                    println("\n------------------------------------------------------------------------")
                }

                else -> println("Opção inválida!")
            }

            // Após a escolha de ação, verifica se o jogo tem vencedor

        }
    }
}