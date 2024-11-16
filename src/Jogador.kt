import kotlin.random.Random

class Jogador(val nome: String) {
    val mao: MutableList<Carta> = mutableListOf() // Cartas do jogador
    val tabuleiro: MutableList<CartaMonstro?> = MutableList(5) { null }// Monstros no tabuleiro
    var pontosDeVida = 10000 // Pontos iniciais
    private var tituloExibido = false
    var podePosicionarMonstro = true
    var equipamentoUsado: Boolean = false


    fun exibirPontosDeVida() { // Função para exibir os pontos de vida do jogador
        println("$nome, seus pontos de vida atuais são: $pontosDeVida")
    }

    fun receberCarta(carta: Carta) {  // Função para o jogador receber uma carta
        if (!tituloExibido) {
            println("\n\uD83D\uDC7B || Jogo de Cartas Monstro || \uD83D\uDC09")
            tituloExibido = true
        }
        mao.add(carta)// Adiciona a carta à mão do jogador
        val tipo = when (carta) { // Verifica o tipo de carta e mostra ao jogador o tipo de carta recebida
            is CartaEquipamento -> {
                println("$nome recebeu a carta: ${carta.nome} : ${carta.obterTipo()} (Ataque: ${carta.bonusAtaque}, Defesa: ${carta.bonusDefesa}")
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
                println("1) Ataque")
                println("2) Defesa")
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
//            println("Espaços livres no tabuleiro: ${espacosLivres.joinToString(", ")}")

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
        if (dado > 17) {
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
//                println("Espaços livres no tabuleiro: ${espacosLivres.joinToString(", ")}")

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





    // Função para realizar um ataque
    fun realizarAtaque(jogadorAdversario: Jogador) {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()

        if (monstros.isNotEmpty()) {
            // Mostra os monstros que o jogador pode escolher para atacar
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} (Ataque: ${carta.ataque}, Defesa: ${carta.defesa}, Já atacou: ${carta.jaAtacou}, Estado: ${carta.estado})")
            }

            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
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
                                val danoRecebido:Int = monstroEscolhido.ataque - monstroAdversarioEscolhido.defesa
                                // O adversário perde 2000 pontos de vida
                                jogadorAdversario.pontosDeVida -= danoRecebido  // Regra bônus(Paloma e Thiago)
                                println("${jogadorAdversario.nome} perdeu ${danoRecebido} pontos de vida!")
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
                        // Marca o monstro como já tendo atacado
                        monstroEscolhido.jaAtacou = true

                        // Exibe a pontuação de vida atual de ambos os jogadores
                        mostrarPontuacaoAtual(jogadorAdversario)

                    } else {
                        println("Monstro adversário inválido para atacar.")
                    }
                } else {
                    println("O adversário não tem monstros para atacar, então recebeu ataque direto !")

                    val dano = monstroEscolhido.ataque
                    jogadorAdversario.pontosDeVida -= dano
                    mostrarPontuacaoAtual(jogadorAdversario)

                }
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

                    println("$nome equipou ${monstroEscolhido.nome} com ${equipamentoEscolhido.nome}!")
                    println("${monstroEscolhido.nome} agora tem Ataque: ${monstroEscolhido.ataque} e Defesa: ${monstroEscolhido.defesa}")
                } else {
                    println("Equipamento inválido.")
                }
            } else {
                println("Monstro inválido.")
            }
        } else {
            println("Você não tem monstros ou equipamentos disponíveis!")
        }
    }

    fun finalizarVez(){
        return
    }

    fun alterarEstadoMonstro() { // Função para alterar o estado de um monstro no tabuleiro
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para alterar o estado:")
            monstros.forEachIndexed { index, carta ->
                println("${index + 1}) ${carta.nome} - Estado atual: ${carta.estado}")
            }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
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
    fun descartarCarta() {
        if (mao.size > 10) {
            println("Você tem mais de 10 cartas na mão. Você precisa descartar algumas.")
            while (mao.size > 10) {
                println("Escolha uma carta para descartar:")
                mao.forEachIndexed { index, carta -> println("${index + 1} ${carta.nome}: ${carta.tipo}  (Ataque: ${carta.ataque}, Defesa: ${carta.defesa})") }
                val escolha = readLine()?.toIntOrNull()
                val cartaEscolhida = mao.getOrNull(escolha?.minus(1) ?: -1)

                if (cartaEscolhida != null) {
                    mao.remove(cartaEscolhida)
                    println("${cartaEscolhida.nome} : ${cartaEscolhida.tipo} foi descartada!")
                } else {
                    println("Opção inválida.")
                }
            }
        } else {
            println("Você já está dentro do limite de 10 cartas na mão.")
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

        while (continuarJogando) {
            println("1)\uD83D\uDC09Posicionar um novo monstro no tabuleiro")
            println("2)⚙\uFE0FEquipar um monstro com uma carta de equipamento")
            println("3)\uD83D\uDDD1\uFE0FDescartar uma carta da mão")
            println("4)⚔\uFE0FRealizar um ataque contra o oponente")
            println("5)\uD83D\uDD04 Alterar o estado de um monstro (ataque/defesa)")
            println("6)\uD83D\uDC4B Finalizar vez")
            println("7)\uD83D\uDC4B Sair")
            println("$nome, selecione uma opção:")

            val opcao = readLine()?.toIntOrNull()

            if (opcao == null || opcao !in 1..7) {
                println("Opção inválida. Por favor, escolha uma opção de 1 a 7.")
                continue // Pula para a próxima iteração
            }

            when (opcao) {
                1 -> posicionarMonstro()
                2 -> equiparMonstro()
                3 -> descartarCarta()
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
                    jogo.finalizarJogo()
                    continuarJogando = false // Encerra o loop se o jogador escolher sair
                }
                else -> println("Opção inválida!")
            }
        }
    }
}