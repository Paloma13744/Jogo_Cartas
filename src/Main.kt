import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

fun main() {
    val caminhoArquivo = "C:/Users/palom/IdeaProjects/Jogo_Cartas/cartas.csv" // Caminho do arquivo (Obs para professor: Para rodar o jogo troque pelo caminho onde está o arquivo cartas.csv em seu computador)
    val baralho = carregarCartasArquivo(caminhoArquivo)

    if (baralho.size < 10) {
        println("Erro: Baralho possui menos de 10 cartas. Verifique o arquivo de cartas.")
        return
    }
    val jogo = Jogo(baralho)

    // Inicia o jogo
    jogo.iniciarJogo()

}

fun carregarCartasArquivo(arquivo: String): MutableList<Carta> {
    val cartas = mutableListOf<Carta>()
    try {
        val linhas = File(arquivo).readLines(Charsets.ISO_8859_1)
        println("Cartas lidas: ${linhas.size}") // Exibe o número de linhas lidas

        linhas.forEachIndexed { index, linha ->
            // Ignorar linhas vazias ou inválidas
            if (linha.trim().isEmpty()) return@forEachIndexed

            val dados = linha.split(";")
            if (dados.size < 5) {
                println("Linha inválida no arquivo (linha ${index + 1}): $linha")
                return@forEachIndexed
            }

            val nome = dados[0].trim()
            val descricao = dados[1].trim()
            val ataque = dados[2].trim().toIntOrNull() ?: run {
                println("Valor de ataque inválido na linha ${index + 1}: $linha")
                return@forEachIndexed
            }
            val defesa = dados[3].trim().toIntOrNull() ?: run {
                println("Valor de defesa inválido na linha ${index + 1}: $linha")
                return@forEachIndexed
            }
            val tipo = dados[4].trim().lowercase()

            val carta = when (tipo) {  // Cria carta de acordo com o tipo (Monstro ou equipamento)
                "equipamento" -> CartaEquipamento(nome, descricao, ataque, defesa)
                "monstro" -> CartaMonstro(nome, descricao, ataque, defesa)
                else -> {
                    println("Tipo de carta desconhecido na linha ${index + 1}: $tipo")
                    null
                }
            }
            carta?.let { cartas.add(it) } // Se a carta é válida , então é adicionada à lista de cartas.
        }
    } catch (e: FileNotFoundException) {
        println("Arquivo não encontrado: ${e.message}")
    } catch (e: IOException) {
        println("Erro de I/O ao processar o arquivo: ${e.message}")
    } catch (e: Exception) {
        println("Erro inesperado ao processar o arquivo: ${e.message}")
    }
    return cartas  // Retorna a minha lista de cartas
}