// Main function to process the VM files
static void main(String[] args) {
  def handleBuy = { productName, amount, price ,outputFile->
    def buyString = "### BUY $productName ###\n${amount * price}\n"
    outputFile << buyString
  }

  def handleSell = { productName, amount, price ,outputFile->
    def sellString = "\$\$\$ CELL $productName \$\$\$\n${amount * price}\n"
    outputFile << sellString
  }
  println "enter path of folder"
  def path = System.in.newReader().readLine()
  def folder = new File(path)
  def outputFile = new File("${folder.path}/${folder.name}.asm").newWriter()

  folder.eachFile { file ->
    if (file.name.endsWith(".vm")) {
      def fileName = file.name - ".vm"
      outputFile << "$fileName\n"

      file.eachLine { line ->
        def parts = line.split()
        def operation = parts[0]
        def productName = parts[1]
        def amount = parts[2] as Integer
        def price = parts[3] as Double

        if (operation == "buy") {
          handleBuy(productName, amount, price,outputFile)
        } else if (operation == "cell") {
          handleSell(productName, amount, price,outputFile)
        }
      }
    }
  }


  // Calculate and print sums
  // Uncomment the following lines if you intend to use them
  def sumPurchases = folder.listFiles().findAll { it.name.endsWith(".vm") }
          .sum { it.readLines().findAll { line -> line.startsWith("buy") }
                  .sum { line -> line.split()[2] as Integer } }
  def sumSales = folder.listFiles().findAll { it.name.endsWith(".vm") }
          .sum { it.readLines().findAll { line -> line.startsWith("cell") }
                  .sum { line -> line.split()[2] as Integer } }

  println("Sum of all purchases: $sumPurchases")
  println("Sum of all sales: $sumSales")
  outputFile << "Sum of all purchases: $sumPurchases\nSum of all sales: $sumSales"

  outputFile.close()

}
