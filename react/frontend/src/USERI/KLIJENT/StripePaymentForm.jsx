import { useState, useEffect } from "react";
import { loadStripe } from "@stripe/stripe-js";
import {
  Elements,
  CardElement,
  useStripe,
  useElements,
} from "@stripe/react-stripe-js";
import { API_URL } from "../../config";

function StripePaymentFormInner({ racunId, iznos, onSuccess, onCancel }) {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [clientSecret, setClientSecret] = useState(null);
  
  // Osiguraj da je iznos number
  const iznosNum = typeof iznos === 'number' ? iznos : parseFloat(iznos) || 0;

  useEffect(() => {
    // Dohvati Payment Intent
    fetch(`${API_URL}/api/placanje/create-payment-intent/${racunId}`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Greška pri dohvaćanju payment intent");
        }
        return res.json();
      })
      .then((data) => {
        if (data.clientSecret && data.clientSecret !== "mock") {
          setClientSecret(data.clientSecret);
        } else {
          setError("Greška: Payment Intent nije kreiran");
        }
      })
      .catch((err) => {
        console.error("Greška pri dohvaćanju payment intent:", err);
        setError("Greška pri inicijalizaciji plaćanja");
      });
  }, [racunId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!stripe || !elements || !clientSecret) {
      return;
    }

    setLoading(true);
    setError(null);

    const cardElement = elements.getElement(CardElement);

    try {
      const { error: stripeError, paymentIntent } = await stripe.confirmCardPayment(
        clientSecret,
        {
          payment_method: {
            card: cardElement,
          },
        }
      );

      if (stripeError) {
        setError(stripeError.message);
        setLoading(false);
      } else if (paymentIntent && paymentIntent.status === "succeeded") {
        // Potvrdi plaćanje na backend-u
        const res = await fetch(`${API_URL}/api/placanje/confirm-payment/${racunId}`, {
          method: "POST",
          credentials: "include",
        });

        if (res.ok) {
          alert("Plaćanje uspješno! ✅");
          onSuccess();
        } else {
          setError("Greška pri potvrdi plaćanja");
        }
        setLoading(false);
      }
    } catch (err) {
      console.error("Greška:", err);
      setError("Greška pri plaćanju. Pokušajte ponovno.");
      setLoading(false);
    }
  };

  const cardElementOptions = {
    style: {
      base: {
        fontSize: "16px",
        color: "#424770",
        "::placeholder": {
          color: "#aab7c4",
        },
      },
      invalid: {
        color: "#9e2146",
      },
    },
  };

  // Ako još nema client secret, prikaži loading
  if (!clientSecret) {
    return (
      <div style={{ padding: "20px" }}>
        <p>Učitavanje forme za plaćanje...</p>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: "500px", margin: "20px 0" }}>
      <h3>Unesite podatke kartice</h3>
      
      <div style={{ 
        padding: "15px", 
        border: "1px solid #ddd", 
        borderRadius: "4px",
        marginBottom: "15px",
        backgroundColor: "#f9f9f9"
      }}>
        <CardElement options={cardElementOptions} />
      </div>

      {error && (
        <div style={{ color: "red", marginBottom: "15px" }}>
          {error}
        </div>
      )}

      <div style={{ display: "flex", gap: "10px" }}>
        <button
          type="submit"
          disabled={!stripe || !elements || loading}
          style={{
            padding: "12px 24px",
            backgroundColor: loading ? "#ccc" : "#4CAF50",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: loading ? "not-allowed" : "pointer",
            fontSize: "16px",
            flex: 1,
          }}
        >
          {loading ? "Obrađuje se..." : `Plati ${iznosNum.toFixed(2)} EUR`}
        </button>
        <button
          type="button"
          onClick={onCancel}
          disabled={loading}
          style={{
            padding: "12px 24px",
            backgroundColor: "#6c757d",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: loading ? "not-allowed" : "pointer",
            fontSize: "16px",
          }}
        >
          Odustani
        </button>
      </div>

      <p style={{ fontSize: "12px", color: "#666", marginTop: "10px" }}>
        Test kartica: 4242 4242 4242 4242 | CVV: bilo koji 3-cifreni broj | Datum: bilo koji budući datum
      </p>
    </form>
  );
}

function MockPaymentForm({ racunId, iznos, onSuccess, onCancel }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // Osiguraj da je iznos number
  const iznosNum = typeof iznos === 'number' ? iznos : parseFloat(iznos) || 0;

  const handleMockPayment = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const res = await fetch(`${API_URL}/api/placanje/plati/${racunId}`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });
      
      if (res.ok) {
        const result = await res.json();
        alert(`Plaćanje uspješno (Mock mode) ✅\n${result.message || "Račun je označen kao plaćen"}`);
        onSuccess();
      } else {
        const errorText = await res.text();
        setError("Greška: " + errorText);
      }
    } catch (err) {
      console.error("Greška:", err);
      setError("Greška pri plaćanju");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: "500px", margin: "20px 0" }}>
      <h3>Plaćanje računa (Mock Mode)</h3>
      <p>Iznos: <strong>{iznosNum.toFixed(2)} EUR</strong></p>
      <p style={{ fontSize: "14px", color: "#666" }}>
        Stripe nije konfiguriran. Plaćanje će biti simulirano.
      </p>
      
      {error && (
        <div style={{ color: "red", marginBottom: "15px" }}>
          {error}
        </div>
      )}

      <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
        <button
          onClick={handleMockPayment}
          disabled={loading}
          style={{
            padding: "12px 24px",
            backgroundColor: loading ? "#ccc" : "#4CAF50",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: loading ? "not-allowed" : "pointer",
            fontSize: "16px",
            flex: 1,
          }}
        >
          {loading ? "Obrađuje se..." : `Potvrdi plaćanje ${iznosNum.toFixed(2)} EUR`}
        </button>
        <button
          onClick={onCancel}
          disabled={loading}
          style={{
            padding: "12px 24px",
            backgroundColor: "#6c757d",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: loading ? "not-allowed" : "pointer",
            fontSize: "16px",
          }}
        >
          Odustani
        </button>
      </div>
    </div>
  );
}

export default function StripePaymentForm({ racunId, iznos, onSuccess, onCancel }) {
  const [stripePromise, setStripePromise] = useState(null);
  const [isMockMode, setIsMockMode] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Dohvati publishable key i provjeri mode
    fetch(`${API_URL}/api/placanje/create-payment-intent/${racunId}`, {
      method: "POST",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Greška");
        }
        return res.json();
      })
      .then((data) => {
        if (data.mockMode || !data.publishableKey || data.publishableKey === "mock" || data.publishableKey === "") {
          setIsMockMode(true);
        } else if (data.publishableKey) {
          // Učitaj Stripe
          const stripe = loadStripe(data.publishableKey);
          setStripePromise(stripe);
        }
      })
      .catch(() => {
        // Ako ne može dohvatiti, koristit će se mock mode
        setIsMockMode(true);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [racunId]);

  if (loading) {
    return (
      <div style={{ padding: "20px" }}>
        <p>Učitavanje...</p>
      </div>
    );
  }

  // Ako je mock mode, koristi MockPaymentForm
  if (isMockMode || !stripePromise) {
    return (
      <MockPaymentForm
        racunId={racunId}
        iznos={iznos}
        onSuccess={onSuccess}
        onCancel={onCancel}
      />
    );
  }

  return (
    <Elements stripe={stripePromise}>
      <StripePaymentFormInner
        racunId={racunId}
        iznos={iznos}
        onSuccess={onSuccess}
        onCancel={onCancel}
      />
    </Elements>
  );
}
