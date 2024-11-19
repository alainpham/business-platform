document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("userForm");
    const resultDiv = document.getElementById("result");

    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent the form from refreshing the page

        // Get the input values
        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;

        try {
            // Send the POST request to the backend
            const response = await fetch("/api/submit", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ name, email }),
            });

            if (response.ok) {
                const data = await response.json();
                resultDiv.textContent = `Success: ${data.message}`;
            } else {
                resultDiv.textContent = `Error: ${response.statusText}`;
            }
        } catch (error) {
            resultDiv.textContent = `Error: Unable to connect to the server.`;
            console.error(error);
        }
    });
});
