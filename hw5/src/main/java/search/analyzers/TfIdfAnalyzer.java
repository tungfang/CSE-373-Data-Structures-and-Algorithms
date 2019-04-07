package search.analyzers;

// import com.sun.xml.internal.bind.v2.model.core.ID;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
// import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;
import java.net.URI;
// import java.security.KeyPair;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> documentTfIdfVectorsNorm;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentTfIdfVectorsNorm = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> wordPair: this.documentTfIdfVectors) {
            documentTfIdfVectorsNorm.put(wordPair.getKey(), norm(wordPair.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> idfTracker = new ChainedHashDictionary<>();
        IDictionary<String, Double> result = new ChainedHashDictionary<>();
        double initial = Math.log(pages.size());
        for (Webpage currentPage : pages) {
            ISet<String> words = getUniqueWords(currentPage);
            for (String currentWord : words) {
                // if (!idfTracker.containsKey(currentWord)) {
                //     // KVPair's value represents the occurrence
                //     idfTracker.put(currentWord, 1.0);
                // } else {
                //     double occurrence = idfTracker.get(currentWord) + 1.0;
                //     idfTracker.put(currentWord, occurrence);
                // }
                idfTracker.put(currentWord, idfTracker.getOrDefault(currentWord, 0.0) + 1);
            }
        }
        for (KVPair<String, Double> wordPair : idfTracker) {
            String word = wordPair.getKey();
            double occurrence = wordPair.getValue();
            result.put(word, initial - Math.log(occurrence));
        }
        return result;
    }

    // Helper method that accepts a web page and returns a unique set of words from
    // the web page.
    private ISet<String> getUniqueWords(Webpage page) {
        ISet<String> result = new ChainedHashSet<>();
        for (String word : page.getWords()) {
            result.add(word);
        }
        return result;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> result = new ChainedHashDictionary<>();
        double initialScore = (double) 1 / words.size();
        for (String currentWord : words) {
            if (!result.containsKey(currentWord)) { // First time appear of current word
                result.put(currentWord, initialScore); // Put the word in result, with computed TF score
            } else { // Word already appeared before
                result.put(currentWord, result.get(currentWord) + initialScore); // Update its TF score
            }
        }
        return result;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> res = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IList<String> words = page.getWords();
            IDictionary<String, Double> singleVector = computeSingleTfIdfVectors(words);
            res.put(page.getUri(), singleVector);
        }
        return res;
    }

    // Return a single page vector with a given list of words that are only shown in the current page
    private IDictionary<String, Double> computeSingleTfIdfVectors(IList<String> words) {
        IDictionary<String, Double> res = new ChainedHashDictionary<>();
        IDictionary<String, Double> tfScore = computeTfScores(words);
        for (KVPair<String, Double> wordPair : tfScore) {
            String word = wordPair.getKey();
            double tf = wordPair.getValue();
            double idf = 0;
            if (idfScores.containsKey(word)) {
                idf = idfScores.get(word);
            }
            res.put(word, tf * idf);
        }
        return res;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = computeSingleTfIdfVectors(query);
        double numerator = 0.0;
        for (KVPair<String, Double> wordPairs : queryVector) {
            String word = wordPairs.getKey();
            double docWordScore = 0;
            if (documentVector.containsKey(word)) {
                docWordScore = documentVector.get(word);
            }
            double queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = this.documentTfIdfVectorsNorm.get(pageUri) * norm(queryVector);
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }

    // Calculate and return the norm value base on the given vector that has word and its tfidf score.
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> wordPairs: vector) {
            double score = wordPairs.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
