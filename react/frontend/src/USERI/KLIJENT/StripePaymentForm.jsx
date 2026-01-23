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
        if (data.clientSecret) {
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

export default function StripePaymentForm({ racunId, iznos, onSuccess, onCancel }) {
  const [stripePromise, setStripePromise] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Dohvati publishable key
    fetch(`${API_URL}/api/placanje/create-payment-intent/${racunId}`, {
      method: "POST",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Greška pri dohvaćanju payment intent");
        }
        return res.json();
      })
      .then((data) => {
        if (data.publishableKey) {
          // Učitaj Stripe
          const stripe = loadStripe(data.publishableKey);
          setStripePromise(stripe);
        } else {
          setError("Stripe nije konfiguriran. Plaćanje nije moguće.");
        }
      })
      .catch((err) => {
        console.error("Greška:", err);
        setError("Greška pri inicijalizaciji plaćanja. Stripe mora biti konfiguriran.");
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

  if (error || !stripePromise) {
    return (
      <div style={{ padding: "20px", color: "red" }}>
        <p>{error || "Stripe nije konfiguriran. Plaćanje nije moguće."}</p>
        <button
          onClick={onCancel}
          style={{
            padding: "10px 20px",
            backgroundColor: "#6c757d",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
            marginTop: "10px",
          }}
        >
          Odustani
        </button>
      </div>
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
