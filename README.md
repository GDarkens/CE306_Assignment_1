Your task is to apply your IR skills to build a processing pipeline that turns a Web site into structured knowledge. 


•Engineering a Complete System

The system you develop must be able to read Web pages from a specified set of URLs and produce appropriately formatted output.

•HTML Parsing

Before the text can be analysed it is necessary to get rid of the HTML tags.The result will be plain text.  Note that if you simply delete all HTML tags,  you will lose informationsuch as meta tag keywords.  Use an appropriate tool to perform this task.

•Pre-processing: Sentence Splitting, Tokenization and Normalization

The next step should be to transform the input text into a normal form of your choice.  This should include the identi-fication of sentences, bullet points and cells in tables.

•Part-of-Speech Tagging

The input should be tagged with a suitable part-of-speech tagger, sothat the result can then be processed in the next steps.

•Selecting Keywords 

One aim of your system is to identify the words and phrases in the textthat are most useful for indexing purposes.  Your system should remove words which are not useful, suchas very frequent words or stopwords, and identify phrases suitable as index terms.  Apply tf.idf as part ofyour selection and weighting step.

•Stemming or Morphological Analysis 

Writing word stems to the database rather than wordsallows to treat various inflected forms of a word in the same way, i.e.bus and bussesrefer to exactly thesame thing even though they are different
