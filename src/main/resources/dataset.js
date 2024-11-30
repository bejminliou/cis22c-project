/**
 * @class DataSet
 *
 * Utility class ; parse and process data from a specific Wikipedia table of U.S. presidents
 */
class DataSet {
    /**
     * Source URL of the dataset
     * @static
     * @type {string}
     * @default 'https://en.wikipedia.org/wiki/List_of_presidents_of_the_United_States'
     */
    static source = 'https://en.wikipedia.org/wiki/List_of_presidents_of_the_United_States';

    /**
     * Retrieves the context element containing the table body (`<tbody>`)
     * @static
     * @type {HTMLTableSectionElement|null}
     */
    static get context() { return document.querySelector('tbody'); }

    /**
     * Parses rows from table (context element) and returns structured data
     * @static
     * @type {Array<{id: string, name: string}>}
     */
    static get parsed() { return [...this.context.querySelectorAll('tr')].map(row => this.#parse_row([...row.querySelectorAll('a')].map(a => a.text))); }

    /**
     * Parses a single row of data
     * @private
     * @static
     * @param {string[]} row - Array of text content from row links.
     * @returns {{id: string, name: string}} - Parsed row object containing `id` and `name`.
     */
    static #parse_row(row) {
        const [id, icon, name, footnote, source, party] = [...row];

        return { id, name };
    }

    /**
     * Formats the parsed data into a string representation
     * @static
     * @param {number} amt - Number of rows to include, starting from the end.
     * @returns {string} - Formatted string of the selected data rows.
     */
    static str(amt) {
        return this.parsed.reverse().slice(0, amt).map(p => Object.entries(p).map(e => e.join(' ')).join('\n')).join('\n\n');
    }
}

/* 
 * results: DataSet.str(15);
 * from: DataSet.source
 * ---------------------
 * id 46
 * name Joe Biden
 * 
 * id 45
 * name Donald Trump
 * 
 * id 44
 * name Barack Obama
 * 
 * id 43
 * name George Bush (George W. Bush)
 * 
 * id 42
 * name Bill Clinton
 * 
 * id 41
 * name George Bush (George H. W. Bush)
 * 
 * id 40
 * name Ronald Reagan
 * 
 * id 39
 * name Jimmy Carter
 * 
 * id 38
 * name Gerald Ford
 * 
 * id 37
 * name Richard Nixon
 * 
 * id 36
 * name Lyndon B. Johnson
 * 
 * id 35
 * name John F. Kennedy
 * 
 * id 34
 * name Dwight D. Eisenhower
 * 
 * id 33
 * name Harry S. Truman
 * 
 * id 32
 * name Franklin D. Roosevelt
 * */
