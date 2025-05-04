class TypeCheckerTest {


    private AeroScriptParser.StatementContext parseStatement(String expression) {
        AeroScriptLexer lexer = new AeroScriptLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AeroScriptParser parser = new AeroScriptParser(tokens);
        return parser.statement();
    }

    @Test
    void TestPointNegative() {

        TypeChecker typeChecker = new TypeChecker();


        try {
            typeChecker.visitStatement(parseStatement("move to point (50, point (10, 20))"));
            fail("Expected a TypeError to be thrown");
        }
        catch (TypeError e) {
            assertEquals("Type is not number: point(10,20)", e.getMessage());
        }

    }
    @Test
    void TestPointPositive() {

        TypeChecker typeChecker = new TypeChecker();


        assertEquals(null, typeChecker.visitStatement(parseStatement("move to point (50, 20")));

    }

    @Test
    void TestSpeedNegative() {

        TypeChecker typeChecker = new TypeChecker();


        try {
            typeChecker.visitStatement(parseStatement("move to point (100, 100) at speed point (25, 25)"));
            fail("Expected a TypeError to be thrown");
        }
        catch (TypeError e) {
            assertEquals("Expression is not a number: movetopoint(100,100)atspeedpoint(25,25)", e.getMessage());
        }

    }

    @Test
    void TestPointNegative2() {

        TypeChecker typeChecker = new TypeChecker();


        try {
            typeChecker.visitStatement(parseStatement("move to point (random[0, point (10, 15) ], random[0, random]) at speed 10"));
            fail("Expected a TypeError to be thrown");
        }
        catch (TypeError e) {
            assertEquals("Type is not number: point(10,15)", e.getMessage());
        }

    }
}