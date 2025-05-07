describe("User Management E2E Tests", () => {
    beforeEach(() => {
      cy.visit("http://localhost:3000");
    });
  
    it("should display the initial list of users", () => {
      cy.get("[data-cy=user-list]").children().should("have.length", 2);
      cy.get("[data-cy=user-list]").should("contain", "Alice");
      cy.get("[data-cy=user-list]").should("contain", "Bob");
    });
  
    it("should add a new user", () => {
      cy.get("[data-cy=name-input]").type("Charlie");
      cy.get("[data-cy=email-input]").type("charlie@example.com");
      cy.get("[data-cy=add-user-button]").click();
  
      cy.get("[data-cy=user-list]").children().should("have.length", 3);
      cy.get("[data-cy=user-list]").should(
        "contain",
        "Charlie (charlie@example.com)"
      );
    });
  });